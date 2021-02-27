package id.co.datascrip.app_collector_systems.data;

/**
 * Created by alamsyah_putra on 3/29/2017.
 */
public class Data {

    private String id, nama, alamat,rowno;


    public Data() {
    }

    public Data(String id, String nama, String alamat,String rowno) {
        this.id = id;
        this.nama = nama;
        this.alamat = alamat;
        this.rowno = rowno;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String get(String name) {
        return name;
    }

    public void setRowno(String rowno){this.rowno = rowno;}
    public String getRowno(){return rowno;}

}
