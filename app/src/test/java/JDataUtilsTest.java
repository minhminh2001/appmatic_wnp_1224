import android.text.TextUtils;
import android.view.TextureView;

import com.whitelabel.app.utils.JDataUtils;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.hamcrest.MatcherAssert.*;
import org.mockito.Mockito.*;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * Created by Administrator on 2017/6/7.
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest(TextUtils.class)
public class JDataUtilsTest {
    @Before
    public void setUp(){
        PowerMockito.mockStatic(TextUtils.class);
        PowerMockito.when(TextUtils.isEmpty("3")).thenAnswer(new Answer<Boolean>() {
            @Override
            public Boolean answer(InvocationOnMock invocation) throws Throwable {
                 CharSequence a= (CharSequence) invocation.getArguments()[0];
                 return !(a!=null&& a.length()>0);
            }
        });
    }
    @Test
    public  void jdataUtils_formatDouble(){
        Assert.assertEquals(JDataUtils.formatDouble("3"),"3.00");
    }

    @Test
    public void jdataUtils_formatThousand(){
        Assert.assertEquals(JDataUtils.formatThousand("10000"),"10,000");
    }
}
