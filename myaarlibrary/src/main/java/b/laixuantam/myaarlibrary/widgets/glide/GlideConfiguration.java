/*
 * (c) Copyright GoTechCom 2017
 */

package b.laixuantam.myaarlibrary.widgets.glide;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.module.GlideModule;

import java.io.InputStream;

/**

 */

public class GlideConfiguration implements GlideModule
{
    @Override
    public void applyOptions(Context context, GlideBuilder builder)
    {
        // Apply options to the builder here.
    }

    @Override
    public void registerComponents(Context context, Glide glide)
    {
        // register ModelLoaders here.
        glide.register(String.class, InputStream.class, new DataUrlLoader.StreamFactory());
    }
}
