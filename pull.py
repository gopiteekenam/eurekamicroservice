import time
import os
import requests
import git
from requests.auth import HTTPBasicAuth
from requests.structures import CaseInsensitiveDict


job_name = "PullReview"  # Give your job name here
last_build = "lastBuild"
headers = {'Accept': 'application/json'}
auth = HTTPBasicAuth('admin', '115f7f4212045adcb3e87c3e36f40d8fb4')
jenkins_url="http://192.168.0.194:8080"
failed= "false"

def extractsha(data):
    actions = data['actions']
    for item in actions:
        if item.get("remoteUrls") is not None:
            repo_list = item.get("lastBuiltRevision").get("branch")
            for repo_info in repo_list:
                sha1 = repo_info.get("SHA1")
                return sha1


def jenkins_job_status(last_build,jenkins_url,failed):
    try:
        url = "{0}/job/PullReview/{1}/api/json".format(jenkins_url,last_build)
        while True:
            data = requests.get(url, headers=headers, auth=auth).json()
            previousbuild = data['previousBuild']
            last_build = previousbuild['number']
            if data['building']:
                time.sleep(60)
            else:
                if data['result'] == "SUCCESS" or data['result'] == "UNSTABLE":
                    print(failed)
                    if failed == "true":
                        print("Job is success")
                        sha1 = extractsha(data)
                        url = revert_changes(data, sha1,failed)

                    exit(0)
                else:
                    print("Job status failed")
                    print("inside failure")
                    sha1 = extractsha(data)
                    revert_changes(data,sha1,failed)
                    failed="true"
                    jenkins_job_status(last_build,jenkins_url,failed)


    except Exception as e:
        print(e)
        return False


def revert_changes(data, sha1,failed):
    actions = data['actions']
    for item in actions:
        if item.get("remoteUrls") is not None:
            remoteURLs = item["remoteUrls"]
            for url in remoteURLs:
                remoteURL = url
            cwd = os.getcwd()
            if not os.path.exists(cwd + "/my_repo"):
                new_repo = git.Repo.clone_from(remoteURL, 'my_repo')
                print(new_repo)
            my_repo = git.Repo('my_repo')
            my_repo.git.reset('--soft', sha1)
            if data['result'] == "SUCCESS" or data['result'] == "UNSTABLE":
                if str(my_repo.active_branch.name) != "reverted_branch":
                    my_repo.git.branch('reverted_branch')
                    my_repo.git.checkout('reverted_branch')
                print("diff is",my_repo.git.diff(my_repo.head.commit.tree))
                my_repo.git.add(u=True)
                my_repo.index.commit('committed the sha')
                my_repo.git.push('--force','origin', 'reverted_branch')
                print("after push")
                print(remoteURL)
                pull_url="https://api.github.com/repos/gopiteekenam/eurekamicroservice/pulls"
                pull_data = '{"title":"Add","body":"Split","base":"master","head":"reverted_branch"}'
                pull_headers = CaseInsensitiveDict()
                pull_headers["Accept"] = "application/vnd.github.v3+json"
                pull_headers["Authorization"] = "Bearer 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"
                res = requests.get(pull_url, headers=pull_headers, data=pull_data).json()
                if len(res) == 0:
                    res = requests.post(pull_url, headers=pull_headers, data=pull_data)
                    print(res.json())
                # g = Github(base_url="https://api.github.com", login_or_token="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")
                # # g = Github("gopiteekenam", "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")
                # print(g)
                # repo = g.get_repo(220450788)
                # repo.create_pull("Add","Split",'',"master","reverted_branch")
                # print('url is',repo.url)
                # print(res.json())
                # my_repo.git.checkout('master')
                # my_repo.delete_head("reverted_branch")
    return url


if __name__ == "__main__":

    if jenkins_job_status(last_build,jenkins_url,failed):

        print("Put your automation here for 'job is success' condition")

    else:
        print("Put your automation here for 'job is failed' condition")