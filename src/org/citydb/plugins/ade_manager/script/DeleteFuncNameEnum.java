package org.citydb.plugins.ade_manager.script;

public enum DeleteFuncNameEnum {
	delete_surface_geometry("delete_surface_geometry"),
	delete_implicit_geometry("delete_implicit_geometry"),
	delete_grid_coverage("delete_grid_coverage"),
	delete_citymodel("delete_citymodel"),
	delete_genericattrib("delete_genericattrib"),
	delete_external_reference("delete_external_reference"),
	delete_appearance("delete_appearance"),
	delete_surface_data("delete_surface_data"),
	
	delete_cityobjectgroup("delete_cityobjectgroup"),
	delete_thematic_surface("delete_thematic_surface"),
	delete_opening("delete_opening"),
	delete_address("delete_address"),
	delete_building_installation("delete_building_installation"),
	delete_room("delete_room"),
	delete_building_furniture("delete_building_furniture"),
	delete_building("delete_building"),
	
	delete_city_furniture("delete_city_furniture"),
	delete_generic_cityobject("delete_generic_cityobject"),
	delete_land_use("delete_land_use"),
	delete_plant_cover("delete_plant_cover"),
	delete_solitary_veg_obj("delete_solitary_veg_obj"),
	delete_transport_complex("delete_transport_complex"),
	delete_traffic_area("delete_traffic_area"),
	delete_waterbnd_surface("delete_waterbnd_surface"),
	
	delete_waterbody("delete_waterbody"),
	delete_relief_feature("delete_relief_feature"),
	delete_relief_component("delete_relief_component"),
	delete_tin_relief("delete_tin_relief"),
	delete_masspoint_relief("delete_masspoint_relief"),
	delete_breakline_relief("delete_breakline_relief"),
	delete_raster_relief("delete_raster_relief"),
	delete_bridge("delete_bridge"),
	
	delete_bridge_installation("delete_bridge_installation"),
	delete_bridge_thematic_surface("delete_bridge_thematic_surface"),
	delete_bridge_opening("delete_bridge_opening"),
	delete_bridge_furniture("delete_bridge_furniture"),
	delete_bridge_room("delete_bridge_room"),
	delete_bridge_constr_element("delete_bridge_constr_element"),
	delete_tunnel("delete_tunnel"),
	delete_tunnel_installation("delete_tunnel_installation"),
	
	delete_tunnel_thematic_surface("delete_tunnel_thematic_surface"),
	delete_tunnel_opening("delete_tunnel_opening"),
	delete_tunnel_furniture("delete_tunnel_furniture"),
	delete_tunnel_hollow_space("delete_tunnel_hollow_space"),
	delete_cityobject("delete_cityobject");
	
	private final String value;

	DeleteFuncNameEnum(String v) {
        value = v;
    }

    public String value() {
        return value;
    }
    
    public String toString() {
		return value;
	}
}
