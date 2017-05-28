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
    certificationName VARCHAR(254),
    certificationid VARCHAR(254),
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
    totalValue_uom VARCHAR(254),
    totalValue NUMERIC,
    PRIMARY KEY (ID)
);

-- -------------------------------------------------------------------- 
-- Test_IndustrialBuilding 
-- -------------------------------------------------------------------- 
CREATE TABLE Test_IndustrialBuilding
(
    ID INTEGER NOT NULL,
    remark VARCHAR(254),
    PRIMARY KEY (ID)
);

-- -------------------------------------------------------------------- 
-- Test_building 
-- -------------------------------------------------------------------- 
CREATE TABLE Test_building
(
    ID INTEGER NOT NULL,
    ownerName VARCHAR(254),
    EnergyPerforman_certificationN VARCHAR(254),
    floorArea_uom VARCHAR(254),
    floorArea NUMERIC,
    EnergyPerforman_certificationi VARCHAR(254),
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
-- -------------------------------------------------------------------- 
-- Test_BuildingUnit 
-- -------------------------------------------------------------------- 
CREATE INDEX Test_BuildingUn_Objectclas_FKX ON Test_BuildingUnit
    USING btree
    (
      OBJECTCLASS_ID ASC NULLS LAST
    )   WITH (FILLFACTOR = 90);

CREATE INDEX Test_Buildin_buildi_buildi_FKX ON Test_BuildingUnit
    USING btree
    (
      building_buildingUnit_ID ASC NULLS LAST
    )   WITH (FILLFACTOR = 90);

CREATE INDEX Test_BuildingUnit_Parent_FKX ON Test_BuildingUnit
    USING btree
    (
      BuildingUnit_Parent_ID ASC NULLS LAST
    )   WITH (FILLFACTOR = 90);

CREATE INDEX Test_BuildingUnit_Root_FKX ON Test_BuildingUnit
    USING btree
    (
      BuildingUnit_Root_ID ASC NULLS LAST
    )   WITH (FILLFACTOR = 90);

CREATE INDEX Test_BuildingUn_lod2MultiC_SPX ON Test_BuildingUnit
    USING gist
    (
      lod2MultiCurve
    );

CREATE INDEX Test_BuildingUn_lod3MultiC_SPX ON Test_BuildingUnit
    USING gist
    (
      lod3MultiCurve
    );

CREATE INDEX Test_BuildingUn_lod4MultiC_SPX ON Test_BuildingUnit
    USING gist
    (
      lod4MultiCurve
    );

CREATE INDEX Test_BuildingUn_lod1MultiS_FKX ON Test_BuildingUnit
    USING btree
    (
      lod1MultiSurface_ID ASC NULLS LAST
    )   WITH (FILLFACTOR = 90);

CREATE INDEX Test_BuildingUn_lod2MultiS_FKX ON Test_BuildingUnit
    USING btree
    (
      lod2MultiSurface_ID ASC NULLS LAST
    )   WITH (FILLFACTOR = 90);

CREATE INDEX Test_BuildingUn_lod3MultiS_FKX ON Test_BuildingUnit
    USING btree
    (
      lod3MultiSurface_ID ASC NULLS LAST
    )   WITH (FILLFACTOR = 90);

CREATE INDEX Test_BuildingUn_lod4MultiS_FKX ON Test_BuildingUnit
    USING btree
    (
      lod4MultiSurface_ID ASC NULLS LAST
    )   WITH (FILLFACTOR = 90);

CREATE INDEX Test_BuildingUni_lod1Solid_FKX ON Test_BuildingUnit
    USING btree
    (
      lod1Solid_ID ASC NULLS LAST
    )   WITH (FILLFACTOR = 90);

CREATE INDEX Test_BuildingUni_lod2Solid_FKX ON Test_BuildingUnit
    USING btree
    (
      lod2Solid_ID ASC NULLS LAST
    )   WITH (FILLFACTOR = 90);

CREATE INDEX Test_BuildingUni_lod3Solid_FKX ON Test_BuildingUnit
    USING btree
    (
      lod3Solid_ID ASC NULLS LAST
    )   WITH (FILLFACTOR = 90);

CREATE INDEX Test_BuildingUni_lod4Solid_FKX ON Test_BuildingUnit
    USING btree
    (
      lod4Solid_ID ASC NULLS LAST
    )   WITH (FILLFACTOR = 90);

-- -------------------------------------------------------------------- 
-- Test_EnergyPerformanceCertific 
-- -------------------------------------------------------------------- 
CREATE INDEX Test_EnergyP_Buildi_energy_FKX ON Test_EnergyPerformanceCertific
    USING btree
    (
      BuildingUnit_energyPerforma_ID ASC NULLS LAST
    )   WITH (FILLFACTOR = 90);

-- -------------------------------------------------------------------- 
-- Test_Facilities 
-- -------------------------------------------------------------------- 
CREATE INDEX Test_Facilities_Objectclas_FKX ON Test_Facilities
    USING btree
    (
      OBJECTCLASS_ID ASC NULLS LAST
    )   WITH (FILLFACTOR = 90);

CREATE INDEX Test_Facilit_Buildi_equipp_FKX ON Test_Facilities
    USING btree
    (
      BuildingUnit_equippedWith_ID ASC NULLS LAST
    )   WITH (FILLFACTOR = 90);

-- ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ 
-- *********************************  Create Sequences  *********************************** 
-- ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ 

CREATE SEQUENCE Test_EnergyPerformanceCert_SEQ
INCREMENT BY 1
MINVALUE 0
MAXVALUE 2147483647
START WITH 1
CACHE 1
NO CYCLE
OWNED BY NONE;

