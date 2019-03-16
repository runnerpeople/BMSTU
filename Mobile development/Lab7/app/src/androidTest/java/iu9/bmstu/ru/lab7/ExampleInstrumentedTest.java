package iu9.bmstu.ru.lab7;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.app.Fragment;

import org.junit.Test;
import org.junit.runner.RunWith;

import iu9.bmstu.ru.lab7.fragment.SettingsFragment;

import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("iu9.bmstu.ru.lab7", appContext.getPackageName());
    }

    @Test
    public void checkFragment() throws Exception {
//        SettingsFragment fragment = new SettingsFragment();
    }
}
