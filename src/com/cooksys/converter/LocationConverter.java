package com.cooksys.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import com.cooksys.model.Location;

@FacesConverter(value="location")
public class LocationConverter implements Converter {

	/**
	 * Converts the selected Location in the selectOneListBox and 
	 * converts its string to a Location object
	 */
	@Override
	public Object getAsObject(FacesContext context, UIComponent component,
			String value) {
		if (value == null || value.isEmpty()) {
            return null;
        }
		
		String[] cityState = value.split(",");
		
		Location location = new Location();
		
		location.setCity(cityState[0]);
		location.setState(cityState[1]);
		return location;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component,
			Object value) {
		// TODO Auto-generated method stub
		return null;
	}

}
