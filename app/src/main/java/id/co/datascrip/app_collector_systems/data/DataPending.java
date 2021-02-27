package id.co.datascrip.app_collector_systems.data;

/**
 * Created by alamsyah_putra on 4/6/2017.
 */
public class DataPending {
    private String id, fakturno,cust_no,cust_name,hasildesc,hasilkunjung,jampickup,jammulai,jamselesai,alamat;

    public DataPending(){

    }

    public DataPending(String id, String fakturno, String cust_no, String cust_name, String hasildesc, String hasilkunjung
            , String jampickup, String jammulai, String jamselesai, String alamat) {
        this.id = id;
        this.fakturno = fakturno;
        this.cust_no = cust_no;
        this.cust_name = cust_name;
        this.hasildesc = hasildesc;
        this.hasilkunjung = hasilkunjung;
        this.jampickup = jampickup;
        this.jammulai = jammulai;
        this.jamselesai = jamselesai;
        this.alamat = alamat;
    }

    public String getAlamat(){return  alamat;}
    public void setAlamat(String alamat){ this.alamat = alamat;}

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getFakturno(){return fakturno;}
    public void setFakturno(String faktur){this.fakturno = faktur;}

    public String getCust_no(){return cust_no;}
    public void setCust_no(String cust_no) {
        this.cust_no = cust_no;
    }

    public String getCust_name() {
        return cust_name;
    }
    public void setCust_name(String cust_name) {
        this.cust_name = cust_name;
    }

    public String getHasildesc() {
        return hasildesc;
    }
    public void setHasildesc(String hasildesc) {
        this.hasildesc = hasildesc;
    }

    public String getHasilkunjung() {
        return hasilkunjung;
    }
    public void setHasilkunjung(String hasilkunjung) {
        this.hasilkunjung = hasilkunjung;
    }

    public String getJammulai() {
        return jammulai;
    }
    public void setJammulai(String jammulai) {
        this.jammulai = jammulai;
    }

    public String getJampickup() {
        return jampickup;
    }
    public void setJampickup(String jampickup) {
        this.jampickup = jampickup;
    }

    public String getJamselesai() {
        return jamselesai;
    }
    public void setJamselesai(String jamselesai) {
        this.jamselesai = jamselesai;
    }
}
