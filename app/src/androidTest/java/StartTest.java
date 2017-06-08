import android.support.test.espresso.Espresso;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.whitelabel.app.R;
import com.whitelabel.app.activity.StartActivity;
import com.whitelabel.app.ui.start.StartActivityV2;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by Administrator on 2017/6/7.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class StartTest {
    private String mStringToBetyped;    @Rule
    public ActivityTestRule<StartActivityV2> mActivityRule = new ActivityTestRule<>(
            StartActivityV2.class);

    @Before
    public void initValidString() {
        // Specify a valid string.
        mStringToBetyped = "Espresso";
    }
    @Test
    public void changeText_sameActivity() {
        // Type text and then press the button.
        Espresso.onView(ViewMatchers.withId(R.id.start_logo_imageview))
                .perform(ViewActions.typeText(mStringToBetyped), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.start_logo_imageview)).perform(ViewActions.click());
        // Check that the text was changed.
        Espresso.onView(ViewMatchers.withId(R.id.start_logo_imageview))
                .check(ViewAssertions.matches(ViewMatchers.withText(mStringToBetyped)));
    }


}
