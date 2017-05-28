-- ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ 
-- ***********************************  Create tables ************************************* 
-- ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ 
-- -------------------------------------------------------------------- 
-- Test_BuildingUnit 
-- -------------------------------------------------------------------- 
CREATE TABLE Test_BuildingUnit
(
    ID INTEGER NOT NULL,
    OBJECTCLASS_ID INTEGER,
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
-- Test_BuildingUnit_to_address 
-- -------------------------------------------------------------------- 
CREATE TABLE Test_BuildingUnit_to_address
(
    BuildingUnit_ID INTEGER NOT NULL,
    address_ID INTEGER NOT NULL,
    PRIMARY KEY (BuildingUnit_ID, address_ID)
);

-- -------------------------------------------------------------------- 
-- Test_EnergyPerformanceCertific 
-- -------------------------------------------------------------------- 
CREATE TABLE Test_EnergyPerformanceCertific
(
    ID INTEGER NOT NULL,
    BuildingUnit_energyPerforma_ID INTEGER,
    certificationName VARCHAR2(254),
    certificationid VARCHAR2(254),
    PRIMARY KEY (ID)
);

-- -------------------------------------------------------------------- 
-- Test_Facilities 
-- -------------------------------------------------------------------- 
CREATE TABLE Test_Facilities
(
    ID INTEGER NOT NULL,
    OBJECTCLASS_ID INTEGER,
    BuildingUnit_equippedWith_ID INTEGER,
    totalValue_uom VARCHAR2(254),
    totalValue NUMBER,
    PRIMARY KEY (ID)
);

-- -------------------------------------------------------------------- 
-- Test_IndustrialBuilding 
-- -------------------------------------------------------------------- 
CREATE TABLE Test_IndustrialBuilding
(
    ID INTEGER NOT NULL,
    remark VARCHAR2(254),
    PRIMARY KEY (ID)
);

-- -------------------------------------------------------------------- 
-- Test_building 
-- -------------------------------------------------------------------- 
CREATE TABLE Test_building
(
    ID INTEGER NOT NULL,
    ownerName VARCHAR2(254),
    EnergyPerforman_certificationN VARCHAR2(254),
    floorArea_uom VARCHAR2(254),
    floorArea NUMBER,
    EnergyPerforman_certificationi VARCHAR2(254),
    PRIMARY KEY (ID)
);

-- ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ 
-- *********************************  Create foreign keys  ******************************** 
-- ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ 
-- -------------------------------------------------------------------- 
-- Test_BuildingUnit 
-- -------------------------------------------------------------------- 
ALTER TABLE Test_BuildingUnit
    ADD CONSTRAINT Test_BuildingUni_Objectclas_FK FOREIGN KEY (OBJECTCLASS_ID) REFERENCES objectclass (ID);

ALTER TABLE Test_BuildingUnit
    ADD CONSTRAINT Test_BuildingUnit_FK FOREIGN KEY (ID) REFERENCES cityobject (ID);

ALTER TABLE Test_BuildingUnit
    ADD CONSTRAINT Test_Buildin_buildin_buildi_FK FOREIGN KEY (building_buildingUnit_ID) REFERENCES Test_building (ID);

ALTER TABLE Test_BuildingUnit
    ADD CONSTRAINT Test_BuildingUnit_Parent_FK FOREIGN KEY (BuildingUnit_Parent_ID) REFERENCES Test_BuildingUnit (ID);

ALTER TABLE Test_BuildingUnit
    ADD CONSTRAINT Test_BuildingUnit_Root_FK FOREIGN KEY (BuildingUnit_Root_ID) REFERENCES Test_BuildingUnit (ID);

ALTER TABLE Test_BuildingUnit
    ADD CONSTRAINT Test_BuildingUni_lod1MultiS_FK FOREIGN KEY (lod1MultiSurface_ID) REFERENCES SURFACE_GEOMETRY (ID);

ALTER TABLE Test_BuildingUnit
    ADD CONSTRAINT Test_BuildingUni_lod2MultiS_FK FOREIGN KEY (lod2MultiSurface_ID) REFERENCES SURFACE_GEOMETRY (ID);

ALTER TABLE Test_BuildingUnit
    ADD CONSTRAINT Test_BuildingUni_lod3MultiS_FK FOREIGN KEY (lod3MultiSurface_ID) REFERENCES SURFACE_GEOMETRY (ID);

ALTER TABLE Test_BuildingUnit
    ADD CONSTRAINT Test_BuildingUni_lod4MultiS_FK FOREIGN KEY (lod4MultiSurface_ID) REFERENCES SURFACE_GEOMETRY (ID);

ALTER TABLE Test_BuildingUnit
    ADD CONSTRAINT Test_BuildingUnit_lod1Solid_FK FOREIGN KEY (lod1Solid_ID) REFERENCES SURFACE_GEOMETRY (ID);

ALTER TABLE Test_BuildingUnit
    ADD CONSTRAINT Test_BuildingUnit_lod2Solid_FK FOREIGN KEY (lod2Solid_ID) REFERENCES SURFACE_GEOMETRY (ID);

ALTER TABLE Test_BuildingUnit
    ADD CONSTRAINT Test_BuildingUnit_lod3Solid_FK FOREIGN KEY (lod3Solid_ID) REFERENCES SURFACE_GEOMETRY (ID);

ALTER TABLE Test_BuildingUnit
    ADD CONSTRAINT Test_BuildingUnit_lod4Solid_FK FOREIGN KEY (lod4Solid_ID) REFERENCES SURFACE_GEOMETRY (ID);

-- -------------------------------------------------------------------- 
-- Test_BuildingUnit_to_address 
-- -------------------------------------------------------------------- 
ALTER TABLE Test_BuildingUnit_to_address
    ADD CONSTRAINT Test_BuildingUn_to_address_FK1 FOREIGN KEY (BuildingUnit_ID) REFERENCES Test_BuildingUnit (ID);

ALTER TABLE Test_BuildingUnit_to_address
    ADD CONSTRAINT Test_BuildingUn_to_address_FK2 FOREIGN KEY (address_ID) REFERENCES address (ID);

-- -------------------------------------------------------------------- 
-- Test_EnergyPerformanceCertific 
-- -------------------------------------------------------------------- 
ALTER TABLE Test_EnergyPerformanceCertific
    ADD CONSTRAINT Test_EnergyP_Buildin_energy_FK FOREIGN KEY (BuildingUnit_energyPerforma_ID) REFERENCES Test_BuildingUnit (ID);

-- -------------------------------------------------------------------- 
-- Test_Facilities 
-- -------------------------------------------------------------------- 
ALTER TABLE Test_Facilities
    ADD CONSTRAINT Test_Facilities_Objectclass_FK FOREIGN KEY (OBJECTCLASS_ID) REFERENCES objectclass (ID);

ALTER TABLE Test_Facilities
    ADD CONSTRAINT Test_Facilit_Buildin_equipp_FK FOREIGN KEY (BuildingUnit_equippedWith_ID) REFERENCES Test_BuildingUnit (ID);

-- -------------------------------------------------------------------- 
-- Test_IndustrialBuilding 
-- -------------------------------------------------------------------- 
ALTER TABLE Test_IndustrialBuilding
    ADD CONSTRAINT Test_IndustrialBuilding_FK FOREIGN KEY (ID) REFERENCES building (ID);

-- -------------------------------------------------------------------- 
-- Test_building 
-- -------------------------------------------------------------------- 
ALTER TABLE Test_building
    ADD CONSTRAINT Test_building_FK FOREIGN KEY (ID) REFERENCES building (ID);

-- ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ 
-- *********************************  Create Indexes  ************************************* 
-- ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ 

SET SERVEROUTPUT ON
SET FEEDBACK ON
SET VER OFF

ALTER SESSION set NLS_TERRITORY='AMERICA';
ALTER SESSION set NLS_LANGUAGE='AMERICAN';

VARIABLE SRID NUMBER;
BEGIN
  SELECT SRID INTO :SRID FROM DATABASE_SRS;
END;
/

column mc new_value SRSNO print
select :SRID mc from dual;

prompt Used SRID for spatial indexes: &SRSNO 

-- -------------------------------------------------------------------- 
-- Test_BuildingUnit 
-- -------------------------------------------------------------------- 
CREATE INDEX Test_BuildingUn_Objectclas_FKX ON Test_BuildingUnit (OBJECTCLASS_ID);

CREATE INDEX Test_Buildin_buildi_buildi_FKX ON Test_BuildingUnit (building_buildingUnit_ID);

CREATE INDEX Test_BuildingUnit_Parent_FKX ON Test_BuildingUnit (BuildingUnit_Parent_ID);

CREATE INDEX Test_BuildingUnit_Root_FKX ON Test_BuildingUnit (BuildingUnit_Root_ID);

DELETE FROM USER_SDO_GEOM_METADATA WHERE TABLE_NAME='TEST_BUILDINGUNIT' AND COLUMN_NAME='LOD2MULTICURVE';
INSERT INTO USER_SDO_GEOM_METADATA (TABLE_NAME, COLUMN_NAME, DIMINFO, SRID)
VALUES ('Test_BuildingUnit','lod2MultiCurve',
MDSYS.SDO_DIM_ARRAY(MDSYS.SDO_DIM_ELEMENT('X', 0.000, 10000000.000, 0.0005), MDSYS.SDO_DIM_ELEMENT('Y', 0.000, 10000000.000, 0.0005),MDSYS.SDO_DIM_ELEMENT('Z', -1000, 10000, 0.0005)), &SRSNO);
CREATE INDEX Test_BuildingUn_lod2MultiC_SPX ON Test_BuildingUnit (lod2MultiCurve) INDEXTYPE IS MDSYS.SPATIAL_INDEX;

DELETE FROM USER_SDO_GEOM_METADATA WHERE TABLE_NAME='TEST_BUILDINGUNIT' AND COLUMN_NAME='LOD3MULTICURVE';
INSERT INTO USER_SDO_GEOM_METADATA (TABLE_NAME, COLUMN_NAME, DIMINFO, SRID)
VALUES ('Test_BuildingUnit','lod3MultiCurve',
MDSYS.SDO_DIM_ARRAY(MDSYS.SDO_DIM_ELEMENT('X', 0.000, 10000000.000, 0.0005), MDSYS.SDO_DIM_ELEMENT('Y', 0.000, 10000000.000, 0.0005),MDSYS.SDO_DIM_ELEMENT('Z', -1000, 10000, 0.0005)), &SRSNO);
CREATE INDEX Test_BuildingUn_lod3MultiC_SPX ON Test_BuildingUnit (lod3MultiCurve) INDEXTYPE IS MDSYS.SPATIAL_INDEX;

DELETE FROM USER_SDO_GEOM_METADATA WHERE TABLE_NAME='TEST_BUILDINGUNIT' AND COLUMN_NAME='LOD4MULTICURVE';
INSERT INTO USER_SDO_GEOM_METADATA (TABLE_NAME, COLUMN_NAME, DIMINFO, SRID)
VALUES ('Test_BuildingUnit','lod4MultiCurve',
MDSYS.SDO_DIM_ARRAY(MDSYS.SDO_DIM_ELEMENT('X', 0.000, 10000000.000, 0.0005), MDSYS.SDO_DIM_ELEMENT('Y', 0.000, 10000000.000, 0.0005),MDSYS.SDO_DIM_ELEMENT('Z', -1000, 10000, 0.0005)), &SRSNO);
CREATE INDEX Test_BuildingUn_lod4MultiC_SPX ON Test_BuildingUnit (lod4MultiCurve) INDEXTYPE IS MDSYS.SPATIAL_INDEX;

CREATE INDEX Test_BuildingUn_lod1MultiS_FKX ON Test_BuildingUnit (lod1MultiSurface_ID);

CREATE INDEX Test_BuildingUn_lod2MultiS_FKX ON Test_BuildingUnit (lod2MultiSurface_ID);

CREATE INDEX Test_BuildingUn_lod3MultiS_FKX ON Test_BuildingUnit (lod3MultiSurface_ID);

CREATE INDEX Test_BuildingUn_lod4MultiS_FKX ON Test_BuildingUnit (lod4MultiSurface_ID);

CREATE INDEX Test_BuildingUni_lod1Solid_FKX ON Test_BuildingUnit (lod1Solid_ID);

CREATE INDEX Test_BuildingUni_lod2Solid_FKX ON Test_BuildingUnit (lod2Solid_ID);

CREATE INDEX Test_BuildingUni_lod3Solid_FKX ON Test_BuildingUnit (lod3Solid_ID);

CREATE INDEX Test_BuildingUni_lod4Solid_FKX ON Test_BuildingUnit (lod4Solid_ID);

-- -------------------------------------------------------------------- 
-- Test_EnergyPerformanceCertific 
-- -------------------------------------------------------------------- 
CREATE INDEX Test_EnergyP_Buildi_energy_FKX ON Test_EnergyPerformanceCertific (BuildingUnit_energyPerforma_ID);

-- -------------------------------------------------------------------- 
-- Test_Facilities 
-- -------------------------------------------------------------------- 
CREATE INDEX Test_Facilities_Objectclas_FKX ON Test_Facilities (OBJECTCLASS_ID);

CREATE INDEX Test_Facilit_Buildi_equipp_FKX ON Test_Facilities (BuildingUnit_equippedWith_ID);

-- ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ 
-- *********************************  Create Sequences  *********************************** 
-- ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ 

CREATE SEQUENCE Test_EnergyPerformanceCert_SEQ INCREMENT BY 1 START WITH 1 MINVALUE 1 CACHE 10000;
