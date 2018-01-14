/*
 * Copyright 2017 Victor Albertos
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package data.foundation.net

import data.foundation.MockModel
import io.reactivex.Single
import io.victoralbertos.jolyglot.GsonSpeaker
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.junit.Test
import retrofit2.Response

class NetworkResponseTest {
    private val networkResponseUT: NetworkResponse = NetworkResponse(GsonSpeaker())

    @Test fun Verify_On_Success() {
        val model = MockModel()
        val observer = Single.just(Response.success(model))
                .compose(networkResponseUT.process())
                .test()

        observer.awaitTerminalEvent()
        observer.assertValueCount(1)
        observer.assertValue(model)
    }

    @Test fun Verify_On_Error() {
        val jsonError = "{\"message\":\"such a nice error\"}"
        val responseBody = ResponseBody.create(MediaType.parse("application/json"), jsonError)

        val observer = Single.just(Response.error<MockModel>(404, responseBody))
                .compose(networkResponseUT.process<MockModel>())
                .test()
        observer.awaitTerminalEvent()
        observer.assertNoValues()
        observer.assertErrorMessage("such a nice error")
    }
}