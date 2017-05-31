-- ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ 
-- ***********************************  Create tables ************************************* 
-- ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ 
-- -------------------------------------------------------------------- 
-- test_BuildingUnit 
-- -------------------------------------------------------------------- 
CREATE TABLE test_BuildingUnit
(
    ID INTEGER NOT NULL,
    building_buildingUnit_ID INTEGER,
    BuildingUnit_Parent_ID INTEGER,
    BuildingUnit_Root_ID INTEGER,
    lod2MultiCurve MDSYS.SDO_GEOMETRY,
    lod3MultiCurve MDSYS.SDO_GEOMETRY,
    lod4MultiCurve MDSYS.SDO_GEOMETRY,
    class_uom VARCHAR2(254),
    class VARCHAR2(254),
    usage_uom VARCHAR2(254),
    usage VARCHAR2(254),
    function_uom VARCHAR2(254),
    function VARCHAR2(254),
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
-- test_BuildingUnit_to_address 
-- -------------------------------------------------------------------- 
CREATE TABLE test_BuildingUnit_to_address
(
    BuildingUnit_ID INTEGER NOT NULL,
    address_ID INTEGER NOT NULL,
    PRIMARY KEY (BuildingUnit_ID, address_ID)
);

-- -------------------------------------------------------------------- 
-- test_EnergyPerformanceCertific 
-- -------------------------------------------------------------------- 
CREATE TABLE test_EnergyPerformanceCertific
(
    ID INTEGER NOT NULL,
    BuildingUnit_energyPerforma_ID INTEGER,
    certificationName VARCHAR2(254),
    certificationid VARCHAR2(254),
    PRIMARY KEY (ID)
);

-- -------------------------------------------------------------------- 
-- test_Facilities 
-- -------------------------------------------------------------------- 
CREATE TABLE test_Facilities
(
    ID INTEGER NOT NULL,
    BuildingUnit_equippedWith_ID INTEGER,
    totalValue_uom VARCHAR2(254),
    totalValue NUMBER,
    PRIMARY KEY (ID)
);

-- -------------------------------------------------------------------- 
-- test_IndustrialBuilding 
-- -------------------------------------------------------------------- 
CREATE TABLE test_IndustrialBuilding
(
    ID INTEGER NOT NULL,
    remark VARCHAR2(254),
    PRIMARY KEY (ID)
);

-- -------------------------------------------------------------------- 
-- test_IndustrialBuildingPart 
-- -------------------------------------------------------------------- 
CREATE TABLE test_IndustrialBuildingPart
(
    ID INTEGER NOT NULL,
    remark VARCHAR2(254),
    PRIMARY KEY (ID)
);

-- -------------------------------------------------------------------- 
-- test_IndustrialBuildingRoofSur 
-- -------------------------------------------------------------------- 
CREATE TABLE test_IndustrialBuildingRoofSur
(
    ID INTEGER NOT NULL,
    remark VARCHAR2(254),
    PRIMARY KEY (ID)
);

-- -------------------------------------------------------------------- 
-- test_OtherCo_to_themati_surfac 
-- -------------------------------------------------------------------- 
CREATE TABLE test_OtherCo_to_themati_surfac
(
    OtherConstruction_ID INTEGER NOT NULL,
    thematic_surface_ID INTEGER NOT NULL,
    PRIMARY KEY (OtherConstruction_ID, thematic_surface_ID)
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
-- test_building 
-- -------------------------------------------------------------------- 
CREATE TABLE test_building
(
    ID INTEGER NOT NULL,
    floorArea_uom VARCHAR2(254),
    floorArea NUMBER,
    ownerName VARCHAR2(254),
    EnergyPerforman_certificationN VARCHAR2(254),
    EnergyPerforman_certificationi VARCHAR2(254),
    PRIMARY KEY (ID)
);

-- ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ 
-- *********************************  Create foreign keys  ******************************** 
-- ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ 
-- -------------------------------------------------------------------- 
-- test_BuildingUnit 
-- -------------------------------------------------------------------- 
ALTER TABLE test_BuildingUnit
    ADD CONSTRAINT test_BuildingUnit_FK FOREIGN KEY (ID) REFERENCES cityobject (ID);

ALTER TABLE test_BuildingUnit
    ADD CONSTRAINT test_Buildin_buildin_buildi_FK FOREIGN KEY (building_buildingUnit_ID) REFERENCES test_building (ID);

ALTER TABLE test_BuildingUnit
    ADD CONSTRAINT test_BuildingUnit_Parent_FK FOREIGN KEY (BuildingUnit_Parent_ID) REFERENCES test_BuildingUnit (ID);

ALTER TABLE test_BuildingUnit
    ADD CONSTRAINT test_BuildingUnit_Root_FK FOREIGN KEY (BuildingUnit_Root_ID) REFERENCES test_BuildingUnit (ID);

ALTER TABLE test_BuildingUnit
    ADD CONSTRAINT test_BuildingUni_lod1MultiS_FK FOREIGN KEY (lod1MultiSurface_ID) REFERENCES SURFACE_GEOMETRY (ID);

ALTER TABLE test_BuildingUnit
    ADD CONSTRAINT test_BuildingUni_lod2MultiS_FK FOREIGN KEY (lod2MultiSurface_ID) REFERENCES SURFACE_GEOMETRY (ID);

ALTER TABLE test_BuildingUnit
    ADD CONSTRAINT test_BuildingUni_lod3MultiS_FK FOREIGN KEY (lod3MultiSurface_ID) REFERENCES SURFACE_GEOMETRY (ID);

ALTER TABLE test_BuildingUnit
    ADD CONSTRAINT test_BuildingUni_lod4MultiS_FK FOREIGN KEY (lod4MultiSurface_ID) REFERENCES SURFACE_GEOMETRY (ID);

ALTER TABLE test_BuildingUnit
    ADD CONSTRAINT test_BuildingUnit_lod1Solid_FK FOREIGN KEY (lod1Solid_ID) REFERENCES SURFACE_GEOMETRY (ID);

ALTER TABLE test_BuildingUnit
    ADD CONSTRAINT test_BuildingUnit_lod2Solid_FK FOREIGN KEY (lod2Solid_ID) REFERENCES SURFACE_GEOMETRY (ID);

ALTER TABLE test_BuildingUnit
    ADD CONSTRAINT test_BuildingUnit_lod3Solid_FK FOREIGN KEY (lod3Solid_ID) REFERENCES SURFACE_GEOMETRY (ID);

ALTER TABLE test_BuildingUnit
    ADD CONSTRAINT test_BuildingUnit_lod4Solid_FK FOREIGN KEY (lod4Solid_ID) REFERENCES SURFACE_GEOMETRY (ID);

-- -------------------------------------------------------------------- 
-- test_BuildingUnit_to_address 
-- -------------------------------------------------------------------- 
ALTER TABLE test_BuildingUnit_to_address
    ADD CONSTRAINT test_BuildingUn_to_address_FK1 FOREIGN KEY (BuildingUnit_ID) REFERENCES test_BuildingUnit (ID);

ALTER TABLE test_BuildingUnit_to_address
    ADD CONSTRAINT test_BuildingUn_to_address_FK2 FOREIGN KEY (address_ID) REFERENCES address (ID);

-- -------------------------------------------------------------------- 
-- test_EnergyPerformanceCertific 
-- -------------------------------------------------------------------- 
ALTER TABLE test_EnergyPerformanceCertific
    ADD CONSTRAINT test_EnergyP_Buildin_energy_FK FOREIGN KEY (BuildingUnit_energyPerforma_ID) REFERENCES test_BuildingUnit (ID);

-- -------------------------------------------------------------------- 
-- test_Facilities 
-- -------------------------------------------------------------------- 
ALTER TABLE test_Facilities
    ADD CONSTRAINT test_Facilit_Buildin_equipp_FK FOREIGN KEY (BuildingUnit_equippedWith_ID) REFERENCES test_BuildingUnit (ID);

-- -------------------------------------------------------------------- 
-- test_IndustrialBuilding 
-- -------------------------------------------------------------------- 
ALTER TABLE test_IndustrialBuilding
    ADD CONSTRAINT test_IndustrialBuilding_FK FOREIGN KEY (ID) REFERENCES building (ID);

-- -------------------------------------------------------------------- 
-- test_IndustrialBuildingPart 
-- -------------------------------------------------------------------- 
ALTER TABLE test_IndustrialBuildingPart
    ADD CONSTRAINT test_IndustrialBuildingPart_FK FOREIGN KEY (ID) REFERENCES building (ID);

-- -------------------------------------------------------------------- 
-- test_IndustrialBuildingRoofSur 
-- -------------------------------------------------------------------- 
ALTER TABLE test_IndustrialBuildingRoofSur
    ADD CONSTRAINT test_IndustrialBuildingRoof_FK FOREIGN KEY (ID) REFERENCES thematic_surface (ID);

-- -------------------------------------------------------------------- 
-- test_OtherCo_to_themati_surfac 
-- -------------------------------------------------------------------- 
ALTER TABLE test_OtherCo_to_themati_surfac
    ADD CONSTRAINT test_OtherC_to_thema_surfa_FK1 FOREIGN KEY (OtherConstruction_ID) REFERENCES test_OtherConstruction (ID);

ALTER TABLE test_OtherCo_to_themati_surfac
    ADD CONSTRAINT test_OtherC_to_thema_surfa_FK2 FOREIGN KEY (thematic_surface_ID) REFERENCES thematic_surface (ID);

-- -------------------------------------------------------------------- 
-- test_OtherConstruction 
-- -------------------------------------------------------------------- 
ALTER TABLE test_OtherConstruction
    ADD CONSTRAINT test_OtherConstruction_FK FOREIGN KEY (ID) REFERENCES cityobject (ID);

-- -------------------------------------------------------------------- 
-- test_building 
-- -------------------------------------------------------------------- 
ALTER TABLE test_building
    ADD CONSTRAINT test_building_FK FOREIGN KEY (ID) REFERENCES building (ID);

-- ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ 
-- *********************************  Create Indexes  ************************************* 
-- ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ 

SET SERVEROUTPUT ON
SET FEEDBACK ON
SET VER OFF
VARIABLE SRID NUMBER;
BEGIN
  SELECT SRID INTO :SRID FROM DATABASE_SRS;
END;
/

column mc new_value SRSNO print
select :SRID mc from dual;

prompt Used SRID for spatial indexes: &SRSNO 

-- -------------------------------------------------------------------- 
-- test_BuildingUnit 
-- -------------------------------------------------------------------- 
CREATE INDEX test_Buildin_buildi_buildi_FKX ON test_BuildingUnit (building_buildingUnit_ID);

CREATE INDEX test_BuildingUnit_Parent_FKX ON test_BuildingUnit (BuildingUnit_Parent_ID);

CREATE INDEX test_BuildingUnit_Root_FKX ON test_BuildingUnit (BuildingUnit_Root_ID);

DELETE FROM USER_SDO_GEOM_METADATA WHERE TABLE_NAME='TEST_BUILDINGUNIT' AND COLUMN_NAME='LOD2MULTICURVE';
INSERT INTO USER_SDO_GEOM_METADATA (TABLE_NAME, COLUMN_NAME, DIMINFO, SRID)
VALUES ('test_BuildingUnit','lod2MultiCurve',
MDSYS.SDO_DIM_ARRAY(MDSYS.SDO_DIM_ELEMENT('X', 0.000, 10000000.000, 0.0005), MDSYS.SDO_DIM_ELEMENT('Y', 0.000, 10000000.000, 0.0005),MDSYS.SDO_DIM_ELEMENT('Z', -1000, 10000, 0.0005)), &SRSNO);
CREATE INDEX test_BuildingUn_lod2MultiC_SPX ON test_BuildingUnit (lod2MultiCurve) INDEXTYPE IS MDSYS.SPATIAL_INDEX;

DELETE FROM USER_SDO_GEOM_METADATA WHERE TABLE_NAME='TEST_BUILDINGUNIT' AND COLUMN_NAME='LOD3MULTICURVE';
INSERT INTO USER_SDO_GEOM_METADATA (TABLE_NAME, COLUMN_NAME, DIMINFO, SRID)
VALUES ('test_BuildingUnit','lod3MultiCurve',
MDSYS.SDO_DIM_ARRAY(MDSYS.SDO_DIM_ELEMENT('X', 0.000, 10000000.000, 0.0005), MDSYS.SDO_DIM_ELEMENT('Y', 0.000, 10000000.000, 0.0005),MDSYS.SDO_DIM_ELEMENT('Z', -1000, 10000, 0.0005)), &SRSNO);
CREATE INDEX test_BuildingUn_lod3MultiC_SPX ON test_BuildingUnit (lod3MultiCurve) INDEXTYPE IS MDSYS.SPATIAL_INDEX;

DELETE FROM USER_SDO_GEOM_METADATA WHERE TABLE_NAME='TEST_BUILDINGUNIT' AND COLUMN_NAME='LOD4MULTICURVE';
INSERT INTO USER_SDO_GEOM_METADATA (TABLE_NAME, COLUMN_NAME, DIMINFO, SRID)
VALUES ('test_BuildingUnit','lod4MultiCurve',
MDSYS.SDO_DIM_ARRAY(MDSYS.SDO_DIM_ELEMENT('X', 0.000, 10000000.000, 0.0005), MDSYS.SDO_DIM_ELEMENT('Y', 0.000, 10000000.000, 0.0005),MDSYS.SDO_DIM_ELEMENT('Z', -1000, 10000, 0.0005)), &SRSNO);
CREATE INDEX test_BuildingUn_lod4MultiC_SPX ON test_BuildingUnit (lod4MultiCurve) INDEXTYPE IS MDSYS.SPATIAL_INDEX;

CREATE INDEX test_BuildingUn_lod1MultiS_FKX ON test_BuildingUnit (lod1MultiSurface_ID);

CREATE INDEX test_BuildingUn_lod2MultiS_FKX ON test_BuildingUnit (lod2MultiSurface_ID);

CREATE INDEX test_BuildingUn_lod3MultiS_FKX ON test_BuildingUnit (lod3MultiSurface_ID);

CREATE INDEX test_BuildingUn_lod4MultiS_FKX ON test_BuildingUnit (lod4MultiSurface_ID);

CREATE INDEX test_BuildingUni_lod1Solid_FKX ON test_BuildingUnit (lod1Solid_ID);

CREATE INDEX test_BuildingUni_lod2Solid_FKX ON test_BuildingUnit (lod2Solid_ID);

CREATE INDEX test_BuildingUni_lod3Solid_FKX ON test_BuildingUnit (lod3Solid_ID);

CREATE INDEX test_BuildingUni_lod4Solid_FKX ON test_BuildingUnit (lod4Solid_ID);

-- -------------------------------------------------------------------- 
-- test_EnergyPerformanceCertific 
-- -------------------------------------------------------------------- 
CREATE INDEX test_EnergyP_Buildi_energy_FKX ON test_EnergyPerformanceCertific (BuildingUnit_energyPerforma_ID);

-- -------------------------------------------------------------------- 
-- test_Facilities 
-- -------------------------------------------------------------------- 
CREATE INDEX test_Facilit_Buildi_equipp_FKX ON test_Facilities (BuildingUnit_equippedWith_ID);

-- ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ 
-- *********************************  Create Sequences  *********************************** 
-- ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ 

CREATE SEQUENCE test_EnergyPerformanceCert_SEQ INCREMENT BY 1 START WITH 1 MINVALUE 1 CACHE 10000;
