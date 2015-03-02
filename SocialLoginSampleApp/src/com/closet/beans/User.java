package com.closet.beans;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable{
private  String  birthday;
private String  email;
private String  gender;
private String  id;
private String  name;
private String username;
private String wardrobe_name;
public String getUsername() {
	return username;
}

public void setUsername(String username) {
	this.username = username;
}

public String getWardrobe_name() {
	return wardrobe_name;
}

public void setWardrobe_name(String wardrobe_name) {
	this.wardrobe_name = wardrobe_name;
}
private String  profilepic;
private String password;
private String  type;
private String mobile;

public User(Parcel source) {
	this.birthday=source.readString();
	this.email=source.readString();
	this.gender=source.readString();
	this.id=source.readString();
	this.name=source.readString();
	this.profilepic=source.readString();
	this.type=source.readString();
	this.username=source.readString();
	this.wardrobe_name=source.readString();
	this.mobile=source.readString();
}

public User() {
	
}
public User(JSONObject obj) {
	try {
		this.birthday=obj.getString("BirthDate");
	} catch (JSONException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	try {
		this.email=obj.getString("Email");
	} catch (JSONException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	try {
		this.gender=obj.getString("Gender");
	} catch (JSONException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	try {
		this.setMobile(obj.getString("Mobile"));
	} catch (JSONException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	try {
		this.id=obj.getString("UserId");
	} catch (JSONException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	try {
		this.profilepic=obj.getString("Image");
	} catch (JSONException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	try {
		this.type=obj.getString("Type");
	} catch (JSONException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	try {
		this.username=obj.getString("Username");
	} catch (JSONException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	try {
		this.wardrobe_name=obj.getString("WardrobeName");
	} catch (JSONException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}

public String getBirthday() {
	return birthday;
}

public void setBirthday(String birthday) {
	this.birthday = birthday;
}

public String getEmail() {
	return email;
}

public void setEmail(String email) {
	this.email = email;
}

public String getGender() {
	return gender;
}

public void setGender(String gender) {
	this.gender = gender;
}

public String getId() {
	return id;
}

public void setId(String id) {
	this.id = id;
}

public String getName() {
	return name;
}

public void setName(String name) {
	this.name = name;
}

public String getProfilepic() {
	return profilepic;
}

public void setProfilepic(String profilepic) {
	this.profilepic = profilepic;
}

public String getType() {
	return type;
}

public void setType(String type) {
	this.type = type;
}



@Override
public int describeContents() {
	// TODO Auto-generated method stub
	return 0;
}
public static final Parcelable.Creator<User> CREATOR= new Parcelable.Creator<User>(){

	@Override
	public User createFromParcel(Parcel source) {
		
		return new User(source);
	}

	

	@Override
	public User[] newArray(int size) {
		
		return new User[size];
	}
	 
 };

@Override
public void writeToParcel(Parcel arg0, int arg1) {
	arg0.writeString(this.birthday);
	arg0.writeString(this.email);
	arg0.writeString(this.gender);
	arg0.writeString(this.id);
	arg0.writeString(this.name);
	arg0.writeString(this.profilepic);
	arg0.writeString(this.type);
	arg0.writeString(this.username);
	arg0.writeString(this.wardrobe_name);
	arg0.writeString(this.mobile);
}

/**
 * @return the mobile
 */
public String getMobile() {
	return mobile;
}

/**
 * @param mobile the mobile to set
 */
public void setMobile(String mobile) {
	this.mobile = mobile;
}

/**
 * @return the password
 */
public String getPassword() {
	return password;
}

/**
 * @param password the password to set
 */
public void setPassword(String password) {
	this.password = password;
}

}
