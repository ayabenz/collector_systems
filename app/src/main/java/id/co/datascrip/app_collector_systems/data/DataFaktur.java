package id.co.datascrip.app_collector_systems.data;

/**
 * Created by alamsyah_putra on 3/30/2017.
 */
public class DataFaktur {
    private String id, fakturno;
    private Integer tanda,nourut;
    boolean selected = false;

    public DataFaktur(){

    }

    //public DataFaktur(String fakturno,String judul, String ket) {
    public DataFaktur(String id, String fakturno, Integer tanda, Integer nourut, boolean selected) {
        this.id = id;
        this.fakturno = fakturno;
        this.tanda = tanda;
        this.nourut = nourut;
        this.selected = selected;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getInvoceno(){return fakturno;}
    public void setInvoceno(String faktur){this.fakturno = faktur;}

    public Integer getTanda(){ return tanda;}
    public void setTanda(Integer tanda){this.tanda = tanda;}

    public Integer getNourut(){return nourut;}
    public void setNourut(Integer nourut){ this.nourut = nourut;}

}
