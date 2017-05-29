-- ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ 
-- ***********************************  Drop foreign keys ********************************* 
-- ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ 
-- -------------------------------------------------------------------- 
-- tes2_BuildingUnit 
-- -------------------------------------------------------------------- 
ALTER TABLE tes2_BuildingUnit
    DROP CONSTRAINT tes2_BuildingUnit_FK;

ALTER TABLE tes2_BuildingUnit
    DROP CONSTRAINT tes2_BuildingUnit_Parent_FK;

ALTER TABLE tes2_BuildingUnit
    DROP CONSTRAINT tes2_BuildingUnit_Root_FK;

ALTER TABLE tes2_BuildingUnit
    DROP CONSTRAINT tes2_Buildin_buildin_buildi_FK;

ALTER TABLE tes2_BuildingUnit
    DROP CONSTRAINT tes2_BuildingUni_lod1MultiS_FK;

ALTER TABLE tes2_BuildingUnit
    DROP CONSTRAINT tes2_BuildingUni_lod2MultiS_FK;

ALTER TABLE tes2_BuildingUnit
    DROP CONSTRAINT tes2_BuildingUni_lod3MultiS_FK;

ALTER TABLE tes2_BuildingUnit
    DROP CONSTRAINT tes2_BuildingUni_lod4MultiS_FK;

ALTER TABLE tes2_BuildingUnit
    DROP CONSTRAINT tes2_BuildingUnit_lod1Solid_FK;

ALTER TABLE tes2_BuildingUnit
    DROP CONSTRAINT tes2_BuildingUnit_lod2Solid_FK;

ALTER TABLE tes2_BuildingUnit
    DROP CONSTRAINT tes2_BuildingUnit_lod3Solid_FK;

ALTER TABLE tes2_BuildingUnit
    DROP CONSTRAINT tes2_BuildingUnit_lod4Solid_FK;

-- -------------------------------------------------------------------- 
-- tes2_BuildingUnit_to_address 
-- -------------------------------------------------------------------- 
ALTER TABLE tes2_BuildingUnit_to_address
    DROP CONSTRAINT tes2_BuildingUn_to_address_FK1;

ALTER TABLE tes2_BuildingUnit_to_address
    DROP CONSTRAINT tes2_BuildingUn_to_address_FK2;

-- -------------------------------------------------------------------- 
-- tes2_EnergyPerformanceCertific 
-- -------------------------------------------------------------------- 
ALTER TABLE tes2_EnergyPerformanceCertific
    DROP CONSTRAINT tes2_EnergyP_Buildin_energy_FK;

-- -------------------------------------------------------------------- 
-- tes2_Facilities 
-- -------------------------------------------------------------------- 
ALTER TABLE tes2_Facilities
    DROP CONSTRAINT tes2_Facilit_Buildin_equipp_FK;

-- -------------------------------------------------------------------- 
-- tes2_IndustrialBuilding 
-- -------------------------------------------------------------------- 
ALTER TABLE tes2_IndustrialBuilding
    DROP CONSTRAINT tes2_IndustrialBuilding_FK;

-- -------------------------------------------------------------------- 
-- tes2_building 
-- -------------------------------------------------------------------- 
ALTER TABLE tes2_building
    DROP CONSTRAINT tes2_building_FK;

-- ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ 
-- ***********************************  Drop tables  ************************************** 
-- ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ 
-- -------------------------------------------------------------------- 
-- tes2_BuildingUnit 
-- -------------------------------------------------------------------- 
DROP TABLE tes2_BuildingUnit;

-- -------------------------------------------------------------------- 
-- tes2_BuildingUnit_to_address 
-- -------------------------------------------------------------------- 
DROP TABLE tes2_BuildingUnit_to_address;

-- -------------------------------------------------------------------- 
-- tes2_EnergyPerformanceCertific 
-- -------------------------------------------------------------------- 
DROP TABLE tes2_EnergyPerformanceCertific;

-- -------------------------------------------------------------------- 
-- tes2_Facilities 
-- -------------------------------------------------------------------- 
DROP TABLE tes2_Facilities;

-- -------------------------------------------------------------------- 
-- tes2_IndustrialBuilding 
-- -------------------------------------------------------------------- 
DROP TABLE tes2_IndustrialBuilding;

-- -------------------------------------------------------------------- 
-- tes2_building 
-- -------------------------------------------------------------------- 
DROP TABLE tes2_building;

-- ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ 
-- *********************************  Drop Sequences  ************************************* 
-- ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ 

DROP SEQUENCE tes2_EnergyPerformanceCert_SEQ;
