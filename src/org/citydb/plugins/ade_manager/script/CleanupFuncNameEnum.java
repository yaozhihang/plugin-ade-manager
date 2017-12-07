package org.citydb.plugins.ade_manager.script;

public enum CleanupFuncNameEnum {
	cleanup_appearances("cleanup_appearances"),
	cleanup_addresses("cleanup_addresses"),
	cleanup_citymodels("cleanup_citymodels"),
	cleanup_cityobjectgroups("cleanup_cityobjectgroups"),
	cleanup_grid_coverages("cleanup_grid_coverages"),
	cleanup_implicit_geometries("cleanup_implicit_geometries"),
	cleanup_tex_images("cleanup_tex_images"),
	cleanup_schema("cleanup_schema");
	
	private final String value;

	CleanupFuncNameEnum(String v) {
        value = v;
    }

    public String value() {
        return value;
    }
    
    public String toString() {
		return value;
	}
}
