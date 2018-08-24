package b.laixuantam.myaarlibrary.helper;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;

public class FileHelper
{
    private final Context context;

    public FileHelper(Context context)
    {
        this.context = context;
    }

    public String[] getFiles(String path) throws IOException
    {
        return context.getAssets().list(path);
    }

    public File getCacheDir()
    {
        return context.getCacheDir();
    }

    public String getFileContent(String path) throws IOException
    {
        StringBuilder result = new StringBuilder();
        BufferedReader reader = null;

        try
        {
            reader = new BufferedReader(new InputStreamReader(context.getAssets().open(path), "UTF-8"));

            String line;

            while ((line = reader.readLine()) != null)
            {
                result.append(line).append("\n");
            }
        }
        finally
        {
            if (reader != null)
            {
                try
                {
                    reader.close();
                }
                catch (IOException e)
                {
                    // ignore
                }
            }
        }

        return result.toString();
    }

    public InputStream getInputStream(String path) throws IOException
    {
        return context.getAssets().open(path);
    }

    public String readFile(File file) throws IOException
    {
        RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
        byte[] bytes = new byte[(int) randomAccessFile.length()];
        randomAccessFile.readFully(bytes);
        randomAccessFile.close();

        return new String(bytes);
    }

    public void writeFile(File file, String content) throws IOException
    {
        FileOutputStream out = new FileOutputStream(file);
        out.write(content.getBytes());
        out.close();
    }
}