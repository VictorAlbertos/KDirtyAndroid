package app.data.sections.users

import app.data.foundation.net.ApiModule

class GithubUsersApiTestAutomated : GithubUsersApiTest_() {
    override fun githubUsersApi(): GithubUsersApi {
        return ApiModule().provideGithubUsersApi()
    }
}
