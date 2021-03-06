package com.muhammadv2.going_somewhere;

import android.provider.BaseColumns;

import com.muhammadv2.going_somewhere.model.data.TravelsDbContract;

import org.junit.Test;

import java.lang.reflect.Modifier;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ContractClassUnitTest {

    @Test
    public void inner_class_exists() throws Exception {
        Class[] innerClasses = TravelsDbContract.class.getDeclaredClasses();
        assertEquals("There should be 1 Inner class inside the contract class", 4, innerClasses.length);
    }

    @Test
    public void inner_class_type_correct() throws Exception {
        Class[] innerClasses = TravelsDbContract.class.getDeclaredClasses();
        assertEquals("Cannot find inner class to complete unit test", 4, innerClasses.length);

        for (Class entryClass : innerClasses) {
            assertTrue("Inner class should implement the BaseColumns interface", BaseColumns.class.isAssignableFrom(entryClass));
            assertTrue("Inner class should be final", Modifier.isFinal(entryClass.getModifiers()));
            assertTrue("Inner class should be static", Modifier.isStatic(entryClass.getModifiers()));
        }
    }

    @Test
    public void inner_class_members_correct() throws Exception {
        Class[] innerClasses = TravelsDbContract.class.getDeclaredClasses();
        assertEquals("Cannot find inner class to complete unit test", 4, innerClasses.length);
    }

}