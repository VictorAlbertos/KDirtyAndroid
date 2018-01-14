package presentation.foundation

import android.content.Intent
import android.os.Bundle
import android.support.test.runner.AndroidJUnit4
import io.victoralbertos.jolyglot.GsonSpeaker
import org.hamcrest.MatcherAssert
import org.hamcrest.core.Is
import org.junit.Test
import org.junit.runner.RunWith
import presentation.foundation.helpers.extensions.get
import presentation.foundation.helpers.extensions.put

@RunWith(AndroidJUnit4::class)
class IntentUtilTest {
    private val mock = MockModel()

    @Test
    fun verifyElementIntent() {
        val joly = GsonSpeaker()
        val key = "key"

        val serialized = Intent()
                .put(key, mock, joly)
                .get<MockModel>(key, joly)

        MatcherAssert.assertThat(mock.toString(), Is.`is`(serialized.toString()))
    }

    @Test
    fun verifyElementsIntent() {
        val joly = GsonSpeaker()
        val key = "key"
        val mocks = listOf(mock, mock)

        val serializedMocks = Intent()
                .put(key, mocks, joly)
                .get<List<MockModel>>(key, joly)

        serializedMocks!!.forEachIndexed { index, serialized ->
            MatcherAssert.assertThat(mocks[index].toString(), Is.`is`(serialized.toString()))
        }
    }

    @Test
    fun verifyElementBundle() {
        val joly = GsonSpeaker()
        val key = "key"

        val serialized = Bundle()
                .put(key, mock, joly)
                .get<MockModel>(key, joly)

        MatcherAssert.assertThat(mock.toString(), Is.`is`(serialized.toString()))
    }

    @Test
    fun verifyElementsBundle() {
        val joly = GsonSpeaker()
        val key = "key"
        val mocks = listOf(mock, mock)

        val serializedMocks = Bundle()
                .put(key, mocks, joly)
                .get<List<MockModel>>(key, joly)

        serializedMocks!!.forEachIndexed { index, serialized ->
            MatcherAssert.assertThat(mocks[index].toString(), Is.`is`(serialized.toString()))
        }
    }
}