package b.laixuantam.myaarlibrary.model;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;

public class SortBaseModel implements Serializable {

    private String strBase;

    private long valueBase;

    private Date dateBase;

    public String getStrBase() {
        return strBase;
    }

    public void setStrBase(String strBase) {
        this.strBase = strBase;
    }

    public long getValueBase() {
        return valueBase;
    }

    public void setValueBase(long valueBase) {
        this.valueBase = valueBase;
    }

    public Date getDateBase() {
        return dateBase;
    }

    public void setDateBase(Date dateBase) {
        this.dateBase = dateBase;
    }

    /*Comparator for sorting the list by SortBaseModel str*/
    public static Comparator<SortBaseModel> sortAscendingByStr = new Comparator<SortBaseModel>() {

        public int compare(SortBaseModel s1, SortBaseModel s2) {
            String StudentName1 = s1.getStrBase().toUpperCase();
            String StudentName2 = s2.getStrBase().toUpperCase();

            //ascending order
            return StudentName1.compareTo(StudentName2);

        }
    };

    public static Comparator<SortBaseModel> sortDescendingByStr = new Comparator<SortBaseModel>() {

        public int compare(SortBaseModel s1, SortBaseModel s2) {
            String StudentName1 = s1.getStrBase().toUpperCase();
            String StudentName2 = s2.getStrBase().toUpperCase();

            //descending order
            return StudentName2.compareTo(StudentName1);
        }
    };

    /*Comparator for sorting the list by value*/
    public static Comparator<SortBaseModel> sortAscendingByValue = new Comparator<SortBaseModel>() {

        public int compare(SortBaseModel s1, SortBaseModel s2) {

            long rollno1 = s1.getValueBase();
            long rollno2 = s2.getValueBase();

            /*For ascending order*/
            return (int) (rollno1 - rollno2);

        }
    };

    public static Comparator<SortBaseModel> sortDescendingByValue = new Comparator<SortBaseModel>() {

        public int compare(SortBaseModel s1, SortBaseModel s2) {

            long rollno1 = s1.getValueBase();
            long rollno2 = s2.getValueBase();

            /*For descending order*/
            return (int) (rollno2 - rollno1);
        }
    };

    public static Comparator<SortBaseModel> sortDescendingByDate = new Comparator<SortBaseModel>() {

        @Override
        public int compare(SortBaseModel o1, SortBaseModel o2) {
            if (o1.getDateBase().getTime() > o2.getDateBase().getTime()) {
                return -1;
            } else if (o1.getDateBase().getTime() < o2.getDateBase().getTime()) {
                return 1;
            } else {
                return 0;
            }
        }
    };

    public static Comparator<SortBaseModel> sortAscendingByDate = new Comparator<SortBaseModel>() {

        @Override
        public int compare(SortBaseModel o1, SortBaseModel o2) {
            if (o1.getDateBase().getTime() > o2.getDateBase().getTime()) {
                return 1;
            } else if (o1.getDateBase().getTime() < o2.getDateBase().getTime()) {
                return -1;
            } else {
                return 0;
            }
        }
    };

}
