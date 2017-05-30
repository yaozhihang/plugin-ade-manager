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
    class_uom VARCHAR(254),
    class VARCHAR(254),
    usage_uom VARCHAR(254),
    usage VARCHAR(254),
    function_uom VARCHAR(254),
    function VARCHAR(254),
    lod2MultiCurve geometry(GEOMETRYZ),
    lod3MultiCurve geometry(GEOMETRYZ),
    lod4MultiCurve geometry(GEOMETRYZ),
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
    BuildingUnit_equippedWith_ID INTEGER,
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
-- test_building 
-- -------------------------------------------------------------------- 
CREATE TABLE test_building
(
    ID INTEGER NOT NULL,
    floorArea_uom VARCHAR(254),
    floorArea NUMERIC,
    ownerName VARCHAR(254),
    EnergyPerforman_certificationN VARCHAR(254),
    EnergyPerforman_certificationi VARCHAR(254),
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
CREATE INDEX test_Buildin_buildi_buildi_FKX ON test_BuildingUnit
    USING btree
    (
      building_buildingUnit_ID ASC NULLS LAST
    )   WITH (FILLFACTOR = 90);

CREATE INDEX test_BuildingUnit_Parent_FKX ON test_BuildingUnit
    USING btree
    (
      BuildingUnit_Parent_ID ASC NULLS LAST
    )   WITH (FILLFACTOR = 90);

CREATE INDEX test_BuildingUnit_Root_FKX ON test_BuildingUnit
    USING btree
    (
      BuildingUnit_Root_ID ASC NULLS LAST
    )   WITH (FILLFACTOR = 90);

CREATE INDEX test_BuildingUn_lod2MultiC_SPX ON test_BuildingUnit
    USING gist
    (
      lod2MultiCurve
    );

CREATE INDEX test_BuildingUn_lod3MultiC_SPX ON test_BuildingUnit
    USING gist
    (
      lod3MultiCurve
    );

CREATE INDEX test_BuildingUn_lod4MultiC_SPX ON test_BuildingUnit
    USING gist
    (
      lod4MultiCurve
    );

CREATE INDEX test_BuildingUn_lod1MultiS_FKX ON test_BuildingUnit
    USING btree
    (
      lod1MultiSurface_ID ASC NULLS LAST
    )   WITH (FILLFACTOR = 90);

CREATE INDEX test_BuildingUn_lod2MultiS_FKX ON test_BuildingUnit
    USING btree
    (
      lod2MultiSurface_ID ASC NULLS LAST
    )   WITH (FILLFACTOR = 90);

CREATE INDEX test_BuildingUn_lod3MultiS_FKX ON test_BuildingUnit
    USING btree
    (
      lod3MultiSurface_ID ASC NULLS LAST
    )   WITH (FILLFACTOR = 90);

CREATE INDEX test_BuildingUn_lod4MultiS_FKX ON test_BuildingUnit
    USING btree
    (
      lod4MultiSurface_ID ASC NULLS LAST
    )   WITH (FILLFACTOR = 90);

CREATE INDEX test_BuildingUni_lod1Solid_FKX ON test_BuildingUnit
    USING btree
    (
      lod1Solid_ID ASC NULLS LAST
    )   WITH (FILLFACTOR = 90);

CREATE INDEX test_BuildingUni_lod2Solid_FKX ON test_BuildingUnit
    USING btree
    (
      lod2Solid_ID ASC NULLS LAST
    )   WITH (FILLFACTOR = 90);

CREATE INDEX test_BuildingUni_lod3Solid_FKX ON test_BuildingUnit
    USING btree
    (
      lod3Solid_ID ASC NULLS LAST
    )   WITH (FILLFACTOR = 90);

CREATE INDEX test_BuildingUni_lod4Solid_FKX ON test_BuildingUnit
    USING btree
    (
      lod4Solid_ID ASC NULLS LAST
    )   WITH (FILLFACTOR = 90);

-- -------------------------------------------------------------------- 
-- test_EnergyPerformanceCertific 
-- -------------------------------------------------------------------- 
CREATE INDEX test_EnergyP_Buildi_energy_FKX ON test_EnergyPerformanceCertific
    USING btree
    (
      BuildingUnit_energyPerforma_ID ASC NULLS LAST
    )   WITH (FILLFACTOR = 90);

-- -------------------------------------------------------------------- 
-- test_Facilities 
-- -------------------------------------------------------------------- 
CREATE INDEX test_Facilit_Buildi_equipp_FKX ON test_Facilities
    USING btree
    (
      BuildingUnit_equippedWith_ID ASC NULLS LAST
    )   WITH (FILLFACTOR = 90);

-- ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ 
-- *********************************  Create Sequences  *********************************** 
-- ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ 

CREATE SEQUENCE test_EnergyPerformanceCert_SEQ
INCREMENT BY 1
MINVALUE 0
MAXVALUE 2147483647
START WITH 1
CACHE 1
NO CYCLE
OWNED BY NONE;

