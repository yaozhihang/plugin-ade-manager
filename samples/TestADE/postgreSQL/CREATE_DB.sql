-- ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ 
-- ***********************************  Create tables ************************************* 
-- ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ 
-- -------------------------------------------------------------------- 
-- test_BuildingU_to_address 
-- -------------------------------------------------------------------- 
CREATE TABLE test_BuildingU_to_address
(
    BuildingUnit_ID INTEGER NOT NULL,
    address_ID INTEGER NOT NULL,
    PRIMARY KEY (BuildingUnit_ID, address_ID)
);

-- -------------------------------------------------------------------- 
-- test_BuildingUnit 
-- -------------------------------------------------------------------- 
CREATE TABLE test_BuildingUnit
(
    ID INTEGER NOT NULL,
    OBJECTCLASS_ID INTEGER,
    BuildingUnit_Parent_ID INTEGER,
    BuildingUnit_Root_ID INTEGER,
    building_buildingUnit_ID INTEGER,
    lod2MultiCurve geometry(GEOMETRYZ),
    lod3MultiCurve geometry(GEOMETRYZ),
    lod4MultiCurve geometry(GEOMETRYZ),
    class_uom VARCHAR(254),
    class VARCHAR(254),
    usage_uom VARCHAR(254),
    usage VARCHAR(254),
    function_uom VARCHAR(254),
    function VARCHAR(254),
    lod1MultiSurface_ID INTEGER,
    lod2MultiSurface_ID INTEGER,
    lod3MultiSurface_ID INTEGER,
    lod4MultiSurface_ID INTEGER,
    lod1Solid_ID INTEGER,
    lod2Solid_ID INTEGER,
    lod3Solid_ID INTEGER,
    lod4Solid_ID INTEGER,
    PRIMARY KEY (ID)
);

-- -------------------------------------------------------------------- 
-- test_EnergyPerformanceCer 
-- -------------------------------------------------------------------- 
CREATE TABLE test_EnergyPerformanceCer
(
    ID INTEGER NOT NULL,
    BuildingUni_energyPerf_ID INTEGER,
    certificationName VARCHAR(254),
    certificationid VARCHAR(254),
    PRIMARY KEY (ID)
);

-- -------------------------------------------------------------------- 
-- test_Facilities 
-- -------------------------------------------------------------------- 
CREATE TABLE test_Facilities
(
    ID INTEGER NOT NULL,
    OBJECTCLASS_ID INTEGER,
    BuildingUni_equippedWi_ID INTEGER,
    totalValue_uom VARCHAR(254),
    totalValue NUMERIC,
    PRIMARY KEY (ID)
);

-- -------------------------------------------------------------------- 
-- test_IndustrialBuilding 
-- -------------------------------------------------------------------- 
CREATE TABLE test_IndustrialBuilding
(
    ID INTEGER NOT NULL,
    remark VARCHAR(254),
    PRIMARY KEY (ID)
);

-- -------------------------------------------------------------------- 
-- test_IndustrialBuildingPa 
-- -------------------------------------------------------------------- 
CREATE TABLE test_IndustrialBuildingPa
(
    ID INTEGER NOT NULL,
    remark VARCHAR(254),
    PRIMARY KEY (ID)
);

-- -------------------------------------------------------------------- 
-- test_IndustrialBuildingRo 
-- -------------------------------------------------------------------- 
CREATE TABLE test_IndustrialBuildingRo
(
    ID INTEGER NOT NULL,
    remark VARCHAR(254),
    PRIMARY KEY (ID)
);

-- -------------------------------------------------------------------- 
-- test_OtherConstruction 
-- -------------------------------------------------------------------- 
CREATE TABLE test_OtherConstruction
(
    ID INTEGER NOT NULL,
    PRIMARY KEY (ID)
);

-- -------------------------------------------------------------------- 
-- test_Other_to_thema_surfa 
-- -------------------------------------------------------------------- 
CREATE TABLE test_Other_to_thema_surfa
(
    OtherConstruction_ID INTEGER NOT NULL,
    thematic_surface_ID INTEGER NOT NULL,
    PRIMARY KEY (OtherConstruction_ID, thematic_surface_ID)
);

-- -------------------------------------------------------------------- 
-- test_building 
-- -------------------------------------------------------------------- 
CREATE TABLE test_building
(
    ID INTEGER NOT NULL,
    EnergyPerfor_certificatio VARCHAR(254),
    ownerName VARCHAR(254),
    floorArea_uom VARCHAR(254),
    floorArea NUMERIC,
    EnergyPerfo_certificati_1 VARCHAR(254),
    PRIMARY KEY (ID)
);

-- ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ 
-- *********************************  Create foreign keys  ******************************** 
-- ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ 
-- -------------------------------------------------------------------- 
-- test_BuildingU_to_address 
-- -------------------------------------------------------------------- 
ALTER TABLE test_BuildingU_to_address
    ADD CONSTRAINT test_Buildi_to_addres_FK1 FOREIGN KEY (BuildingUnit_ID) REFERENCES test_BuildingUnit (ID);

ALTER TABLE test_BuildingU_to_address
    ADD CONSTRAINT test_Buildi_to_addres_FK2 FOREIGN KEY (address_ID) REFERENCES address (ID);

-- -------------------------------------------------------------------- 
-- test_BuildingUnit 
-- -------------------------------------------------------------------- 
ALTER TABLE test_BuildingUnit
    ADD CONSTRAINT test_Building_Objectcl_FK FOREIGN KEY (OBJECTCLASS_ID) REFERENCES objectclass (ID);

ALTER TABLE test_BuildingUnit
    ADD CONSTRAINT test_BuildingUnit_FK FOREIGN KEY (ID) REFERENCES cityobject (ID);

ALTER TABLE test_BuildingUnit
    ADD CONSTRAINT test_BuildingUn_Parent_FK FOREIGN KEY (BuildingUnit_Parent_ID) REFERENCES test_BuildingUnit (ID);

ALTER TABLE test_BuildingUnit
    ADD CONSTRAINT test_BuildingUnit_Root_FK FOREIGN KEY (BuildingUnit_Root_ID) REFERENCES test_BuildingUnit (ID);

ALTER TABLE test_BuildingUnit
    ADD CONSTRAINT test_Build_build_build_FK FOREIGN KEY (building_buildingUnit_ID) REFERENCES test_building (ID);

ALTER TABLE test_BuildingUnit
    ADD CONSTRAINT test_Building_lod1Mult_FK FOREIGN KEY (lod1MultiSurface_ID) REFERENCES SURFACE_GEOMETRY (ID);

ALTER TABLE test_BuildingUnit
    ADD CONSTRAINT test_Building_lod2Mult_FK FOREIGN KEY (lod2MultiSurface_ID) REFERENCES SURFACE_GEOMETRY (ID);

ALTER TABLE test_BuildingUnit
    ADD CONSTRAINT test_Building_lod3Mult_FK FOREIGN KEY (lod3MultiSurface_ID) REFERENCES SURFACE_GEOMETRY (ID);

ALTER TABLE test_BuildingUnit
    ADD CONSTRAINT test_Building_lod4Mult_FK FOREIGN KEY (lod4MultiSurface_ID) REFERENCES SURFACE_GEOMETRY (ID);

ALTER TABLE test_BuildingUnit
    ADD CONSTRAINT test_Building_lod1Soli_FK FOREIGN KEY (lod1Solid_ID) REFERENCES SURFACE_GEOMETRY (ID);

ALTER TABLE test_BuildingUnit
    ADD CONSTRAINT test_Building_lod2Soli_FK FOREIGN KEY (lod2Solid_ID) REFERENCES SURFACE_GEOMETRY (ID);

ALTER TABLE test_BuildingUnit
    ADD CONSTRAINT test_Building_lod3Soli_FK FOREIGN KEY (lod3Solid_ID) REFERENCES SURFACE_GEOMETRY (ID);

ALTER TABLE test_BuildingUnit
    ADD CONSTRAINT test_Building_lod4Soli_FK FOREIGN KEY (lod4Solid_ID) REFERENCES SURFACE_GEOMETRY (ID);

-- -------------------------------------------------------------------- 
-- test_EnergyPerformanceCer 
-- -------------------------------------------------------------------- 
ALTER TABLE test_EnergyPerformanceCer
    ADD CONSTRAINT test_Energ_Build_energ_FK FOREIGN KEY (BuildingUni_energyPerf_ID) REFERENCES test_BuildingUnit (ID);

-- -------------------------------------------------------------------- 
-- test_Facilities 
-- -------------------------------------------------------------------- 
ALTER TABLE test_Facilities
    ADD CONSTRAINT test_Faciliti_Objectcl_FK FOREIGN KEY (OBJECTCLASS_ID) REFERENCES objectclass (ID);

ALTER TABLE test_Facilities
    ADD CONSTRAINT test_Facil_Build_equip_FK FOREIGN KEY (BuildingUni_equippedWi_ID) REFERENCES test_BuildingUnit (ID);

-- -------------------------------------------------------------------- 
-- test_IndustrialBuilding 
-- -------------------------------------------------------------------- 
ALTER TABLE test_IndustrialBuilding
    ADD CONSTRAINT test_IndustrialBuildin_FK FOREIGN KEY (ID) REFERENCES building (ID);

-- -------------------------------------------------------------------- 
-- test_IndustrialBuildingPa 
-- -------------------------------------------------------------------- 
ALTER TABLE test_IndustrialBuildingPa
    ADD CONSTRAINT test_IndustrialBuild_FK_1 FOREIGN KEY (ID) REFERENCES building (ID);

-- -------------------------------------------------------------------- 
-- test_IndustrialBuildingRo 
-- -------------------------------------------------------------------- 
ALTER TABLE test_IndustrialBuildingRo
    ADD CONSTRAINT test_IndustrialBuild_FK_2 FOREIGN KEY (ID) REFERENCES thematic_surface (ID);

-- -------------------------------------------------------------------- 
-- test_OtherConstruction 
-- -------------------------------------------------------------------- 
ALTER TABLE test_OtherConstruction
    ADD CONSTRAINT test_OtherConstruction_FK FOREIGN KEY (ID) REFERENCES cityobject (ID);

-- -------------------------------------------------------------------- 
-- test_Other_to_thema_surfa 
-- -------------------------------------------------------------------- 
ALTER TABLE test_Other_to_thema_surfa
    ADD CONSTRAINT test_Othe_to_them_sur_FK1 FOREIGN KEY (OtherConstruction_ID) REFERENCES test_OtherConstruction (ID);

ALTER TABLE test_Other_to_thema_surfa
    ADD CONSTRAINT test_Othe_to_them_sur_FK2 FOREIGN KEY (thematic_surface_ID) REFERENCES thematic_surface (ID);

-- -------------------------------------------------------------------- 
-- test_building 
-- -------------------------------------------------------------------- 
ALTER TABLE test_building
    ADD CONSTRAINT test_building_FK FOREIGN KEY (ID) REFERENCES building (ID);

-- ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ 
-- *********************************  Create Indexes  ************************************* 
-- ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ 
-- -------------------------------------------------------------------- 
-- test_BuildingUnit 
-- -------------------------------------------------------------------- 
CREATE INDEX test_Building_Objectc_FKX ON test_BuildingUnit
    USING btree
    (
      OBJECTCLASS_ID ASC NULLS LAST
    )   WITH (FILLFACTOR = 90);

CREATE INDEX test_BuildingU_Parent_FKX ON test_BuildingUnit
    USING btree
    (
      BuildingUnit_Parent_ID ASC NULLS LAST
    )   WITH (FILLFACTOR = 90);

CREATE INDEX test_BuildingUni_Root_FKX ON test_BuildingUnit
    USING btree
    (
      BuildingUnit_Root_ID ASC NULLS LAST
    )   WITH (FILLFACTOR = 90);

CREATE INDEX test_Build_build_buil_FKX ON test_BuildingUnit
    USING btree
    (
      building_buildingUnit_ID ASC NULLS LAST
    )   WITH (FILLFACTOR = 90);

CREATE INDEX test_Building_lod2Mul_SPX ON test_BuildingUnit
    USING gist
    (
      lod2MultiCurve
    );

CREATE INDEX test_Building_lod3Mul_SPX ON test_BuildingUnit
    USING gist
    (
      lod3MultiCurve
    );

CREATE INDEX test_Building_lod4Mul_SPX ON test_BuildingUnit
    USING gist
    (
      lod4MultiCurve
    );

CREATE INDEX test_Building_lod1Mul_FKX ON test_BuildingUnit
    USING btree
    (
      lod1MultiSurface_ID ASC NULLS LAST
    )   WITH (FILLFACTOR = 90);

CREATE INDEX test_Building_lod2Mul_FKX ON test_BuildingUnit
    USING btree
    (
      lod2MultiSurface_ID ASC NULLS LAST
    )   WITH (FILLFACTOR = 90);

CREATE INDEX test_Building_lod3Mul_FKX ON test_BuildingUnit
    USING btree
    (
      lod3MultiSurface_ID ASC NULLS LAST
    )   WITH (FILLFACTOR = 90);

CREATE INDEX test_Building_lod4Mul_FKX ON test_BuildingUnit
    USING btree
    (
      lod4MultiSurface_ID ASC NULLS LAST
    )   WITH (FILLFACTOR = 90);

CREATE INDEX test_Building_lod1Sol_FKX ON test_BuildingUnit
    USING btree
    (
      lod1Solid_ID ASC NULLS LAST
    )   WITH (FILLFACTOR = 90);

CREATE INDEX test_Building_lod2Sol_FKX ON test_BuildingUnit
    USING btree
    (
      lod2Solid_ID ASC NULLS LAST
    )   WITH (FILLFACTOR = 90);

CREATE INDEX test_Building_lod3Sol_FKX ON test_BuildingUnit
    USING btree
    (
      lod3Solid_ID ASC NULLS LAST
    )   WITH (FILLFACTOR = 90);

CREATE INDEX test_Building_lod4Sol_FKX ON test_BuildingUnit
    USING btree
    (
      lod4Solid_ID ASC NULLS LAST
    )   WITH (FILLFACTOR = 90);

-- -------------------------------------------------------------------- 
-- test_EnergyPerformanceCer 
-- -------------------------------------------------------------------- 
CREATE INDEX test_Energ_Build_ener_FKX ON test_EnergyPerformanceCer
    USING btree
    (
      BuildingUni_energyPerf_ID ASC NULLS LAST
    )   WITH (FILLFACTOR = 90);

-- -------------------------------------------------------------------- 
-- test_Facilities 
-- -------------------------------------------------------------------- 
CREATE INDEX test_Faciliti_Objectc_FKX ON test_Facilities
    USING btree
    (
      OBJECTCLASS_ID ASC NULLS LAST
    )   WITH (FILLFACTOR = 90);

CREATE INDEX test_Facil_Build_equi_FKX ON test_Facilities
    USING btree
    (
      BuildingUni_equippedWi_ID ASC NULLS LAST
    )   WITH (FILLFACTOR = 90);

-- ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ 
-- *********************************  Create Sequences  *********************************** 
-- ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ 

CREATE SEQUENCE test_EnergyPerformanc_SEQ
INCREMENT BY 1
MINVALUE 0
MAXVALUE 2147483647
START WITH 1
CACHE 1
NO CYCLE
OWNED BY NONE;


