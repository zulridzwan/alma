package com.zulridzwan.lib.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterItem;

//Intro on parcelable: http://www.javacodegeeks.com/2014/01/android-tutorial-two-methods-of-passing-object-by-intent-serializableparcelable.html
//http://stackoverflow.com/questions/7181526/how-can-i-make-my-custom-objects-be-parcelable
public class MyItem implements ClusterItem, Parcelable {
    private final LatLng mPosition;
    private Integer rowid;
    private String reporterid;
    private String reportername;
    private String address;
    private String town;
    private String district;
    private String state;
    private String details;
    private String filename;
    private String emailaddress;
    private double xc;
    private double yc;


    public MyItem(double lat, double lng) {
    	this.xc = lat;
    	this.yc = lng;

        mPosition = new LatLng(lat, lng);
    }
    
    //for Parcelling create another constructor which corresponds to writeToParcel method
    public MyItem(Parcel in){
    	double ddata[] = new double[2];
    	int idata[]= new int[1];
    	String sdata[] = new String[9];
    	
    	in.readDoubleArray(ddata);
    	in.readIntArray(idata);
    	in.readStringArray(sdata);
    	
    	this.xc = ddata[0];
    	this.yc = ddata[1];
    	this.mPosition=new LatLng(xc, yc);
    	
    	this.rowid = idata[0];
    	
    	this.reporterid = sdata[0];
    	this.reportername = sdata[1];
		this.address = sdata[2];
		this.town = sdata[3];
		this.district = sdata[4];
		this.state = sdata[5];
		this.details = sdata[6];
		this.filename = sdata[7];
		this.emailaddress = sdata[8];
    	
    }
    
    //After parcelable constructor defined
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {

		@Override
		public Object createFromParcel(Parcel source) {
			return new MyItem(source);
		}

		@Override
		public Object[] newArray(int size) {
			return new MyItem[size];
		}
    	
    };
   

    @Override
    public LatLng getPosition() {
        return mPosition;
    }
    
    public void setRowid(Integer number) {
        this.rowid = number;
    }

    public Integer getRowid() {
        return rowid;
    }
    
    public void setReporterid(String val) {
        this.reporterid = val;
    }

    public String getReporterid() {
        return reporterid;
    }
    
    public void setReportername(String val) {
        this.reportername = val;
    }

    public String getReportername() {
        return reportername;
    }
    
    public void setAddress(String val) {
        this.address = val;
    }

    public String getAddress() {
        return address;
    }
    
    public void setTown(String val) {
        this.town = val;
    }

    public String getTown() {
        return town;
    }
    
    public void setDistrict(String val) {
        this.district = val;
    }

    public String getDistrict() {
        return district;
    }
    
    public void setState(String val) {
        this.state = val;
    }

    public String getState() {
        return state;
    }
    
    public void setDetails(String val) {
        this.details = val;
    }

    public String getDetails() {
        return details;
    }
    
    public void setFilename(String val) {
        this.filename = val;
    }

    public String getFilename() {
        return filename;
    }
    
    public void setEmailaddress(String val) {
        this.emailaddress = val;
    }

    public String getEmailaddress() {
        return emailaddress;
    }

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		//Need to add another constructor to this class that takes a single parcel variable
		//The order in which the new constructor reads the parcel must follow the order defined below
		//http://www.paulusworld.com/technical/android-parcelable-objects
		dest.writeDoubleArray(new double[]{
		  this.xc,
		  this.yc
		});
		
		dest.writeIntArray(new int[] {
		  this.rowid
		});
		
		dest.writeStringArray(new String[]{
		  this.reporterid,
		  this.reportername,
		  this.address,
		  this.town,
		  this.district,
		  this.state,
		  this.details,
		  this.filename,
		  this.emailaddress
		});
		
		
	}
	

}
