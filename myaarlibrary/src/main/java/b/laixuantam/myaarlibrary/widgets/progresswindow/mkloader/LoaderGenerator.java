package b.laixuantam.myaarlibrary.widgets.progresswindow.mkloader;

import b.laixuantam.myaarlibrary.widgets.progresswindow.mkloader.type.ClassicSpinner;
import b.laixuantam.myaarlibrary.widgets.progresswindow.mkloader.type.FishSpinner;
import b.laixuantam.myaarlibrary.widgets.progresswindow.mkloader.type.LineSpinner;
import b.laixuantam.myaarlibrary.widgets.progresswindow.mkloader.type.LoaderView;

public class LoaderGenerator {

    public static LoaderView generateLoaderView(int type) {
        switch (type) {
            case 0:
                return new ClassicSpinner();
            case 1:
                return new FishSpinner();
            case 2:
                return new LineSpinner();
            default:
                return new ClassicSpinner();
        }
    }

    public static LoaderView generateLoaderView(String type) {
        switch (type) {
            case "ClassicSpinner":
                return new ClassicSpinner();
            case "FishSpinner":
                return new FishSpinner();
            case "LineSpinner":
                return new LineSpinner();
            default:
                return new ClassicSpinner();
        }
    }
}
