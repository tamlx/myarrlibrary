package b.laixuantam.myaarlibrary.base;

import android.os.Bundle;

import java.io.Serializable;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;

import b.laixuantam.myaarlibrary.helper.MyLog;

public class BaseParameters
{
    protected void bind(BaseFragment<?, ?> fragment)
    {
        bind(new ParameterHolder(fragment));
    }

    protected void bind(BaseActivity<?, ?, ?> activity)
    {
        bind(new ParameterHolder(activity));
    }

    protected void bind(BaseFragmentActivity<?, ?, ?> fragmentActivity)
    {
        bind(new ParameterHolder(fragmentActivity));
    }

    public Bundle bundle()
    {
        Field[] fields = getClass().getDeclaredFields();
        Bundle bundle = new Bundle();
        for (Field field : fields)
        {
            Parameter annotation = field.getAnnotation(Parameter.class);

            if (annotation != null)
            {
                try
                {
                    Class<?> type = field.getType();

                    if (type.equals(int.class))
                    {
                        bundle.putInt(annotation.value(), field.getInt(this));
                    }
                    else if (type.equals(boolean.class))
                    {
                        bundle.putBoolean(annotation.value(), field.getBoolean(this));
                    }
                    else if (type.equals(String.class))
                    {
                        bundle.putString(annotation.value(), (String) field.get(this));
                    }
                    else if (type.equals(Serializable.class))
                    {
                        bundle.putSerializable(annotation.value(), (Serializable) field.get(this));
                    }
                }
                catch (Exception e)
                {
                    MyLog.e(e);
                }
            }
        }
        return bundle;
    }

    private void bind(ParameterHolder holder)
    {
        Field[] fields = getClass().getDeclaredFields();

        for (Field field : fields)
        {
            Parameter annotation = field.getAnnotation(Parameter.class);

            if (annotation != null)
            {
                try
                {
                    Class<?> type = field.getType();

                    if (type.equals(int.class))
                    {
                        int value = holder.getParameter(annotation.value(), annotation.defaultInt());
                        field.setInt(this, value);
                    }
                    else if (type.equals(boolean.class))
                    {
                        boolean value = holder.getParameter(annotation.value(), annotation.defaultBoolean());
                        field.setBoolean(this, value);
                    }
                    else if (type.equals(String.class))
                    {
                        String value = holder.getParameter(annotation.value(), annotation.defaultString());
                        field.set(this, value);
                    }
                    else
                    {
                        Object value = holder.getParameter(annotation.value(), null);
                        field.set(this, value);
                    }
                }
                catch (Exception e)
                {
                    MyLog.e(e);
                }
            }
        }
    }

    public static class ParameterHolder
    {
        private BaseFragment<?, ?> fragment = null;
        private BaseActivity<?, ?, ?> activity = null;
        private BaseFragmentActivity<?, ?, ?> fragmentActivity = null;

        public ParameterHolder(BaseFragment<?, ?> fragment)
        {
            this.fragment = fragment;
        }

        public ParameterHolder(BaseActivity<?, ?, ?> activity)
        {
            this.activity = activity;
        }

        public ParameterHolder(BaseFragmentActivity<?, ?, ?> fragmentActivity)
        {
            this.fragmentActivity = fragmentActivity;
        }

        protected <T> T getParameter(String key, T defaultValue)
        {
            if (fragment != null)
            {
                return fragment.getParameter(key, defaultValue);
            }
            else if (activity != null)
            {
                return activity.getParameter(key, defaultValue);
            }
            else if (fragmentActivity != null)
            {
                return fragmentActivity.getParameter(key, defaultValue);
            }
            else
            {
                return null;
            }
        }
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface Parameter
    {
        String value();

        int defaultInt() default 0;

        boolean defaultBoolean() default false;

        String defaultString() default "";
    }
}