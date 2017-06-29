
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.whitelabel.app.R;
import com.whitelabel.app.activity.StartActivity;
import com.whitelabel.app.ui.productdetail.BindProductActivity;
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
    public ActivityTestRule<BindProductActivity> mActivityRule = new ActivityTestRule<>(
            BindProductActivity.class);

    @Before
    public void initValidString() {
        // Specify a valid string.
        mStringToBetyped = "Espresso";
    }
    @Test
    public void changeText_sameActivity() {
    }


}
