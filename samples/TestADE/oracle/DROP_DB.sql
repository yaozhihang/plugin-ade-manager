-- ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ 
-- ***********************************  Drop foreign keys ********************************* 
-- ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ 
-- -------------------------------------------------------------------- 
-- Test_BuildingUnit 
-- -------------------------------------------------------------------- 
ALTER TABLE Test_BuildingUnit
    DROP CONSTRAINT Test_BuildingUnit_FK;

ALTER TABLE Test_BuildingUnit
    DROP CONSTRAINT Test_BuildingUnit_Parent_FK;

ALTER TABLE Test_BuildingUnit
    DROP CONSTRAINT Test_BuildingUnit_Root_FK;

ALTER TABLE Test_BuildingUnit
    DROP CONSTRAINT Test_Buildin_buildin_buildi_FK;

ALTER TABLE Test_BuildingUnit
    DROP CONSTRAINT Test_BuildingUni_lod1MultiS_FK;

ALTER TABLE Test_BuildingUnit
    DROP CONSTRAINT Test_BuildingUni_lod2MultiS_FK;

ALTER TABLE Test_BuildingUnit
    DROP CONSTRAINT Test_BuildingUni_lod3MultiS_FK;

ALTER TABLE Test_BuildingUnit
    DROP CONSTRAINT Test_BuildingUni_lod4MultiS_FK;

ALTER TABLE Test_BuildingUnit
    DROP CONSTRAINT Test_BuildingUnit_lod1Solid_FK;

ALTER TABLE Test_BuildingUnit
    DROP CONSTRAINT Test_BuildingUnit_lod2Solid_FK;

ALTER TABLE Test_BuildingUnit
    DROP CONSTRAINT Test_BuildingUnit_lod3Solid_FK;

ALTER TABLE Test_BuildingUnit
    DROP CONSTRAINT Test_BuildingUnit_lod4Solid_FK;

-- -------------------------------------------------------------------- 
-- Test_BuildingUnit_to_address 
-- -------------------------------------------------------------------- 
ALTER TABLE Test_BuildingUnit_to_address
    DROP CONSTRAINT Test_BuildingUn_to_address_FK1;

ALTER TABLE Test_BuildingUnit_to_address
    DROP CONSTRAINT Test_BuildingUn_to_address_FK2;

-- -------------------------------------------------------------------- 
-- Test_EnergyPerformanceCertific 
-- -------------------------------------------------------------------- 
ALTER TABLE Test_EnergyPerformanceCertific
    DROP CONSTRAINT Test_EnergyP_Buildin_energy_FK;

-- -------------------------------------------------------------------- 
-- Test_Facilities 
-- -------------------------------------------------------------------- 
ALTER TABLE Test_Facilities
    DROP CONSTRAINT Test_Facilit_Buildin_equipp_FK;

-- -------------------------------------------------------------------- 
-- Test_IndustrialBuilding 
-- -------------------------------------------------------------------- 
ALTER TABLE Test_IndustrialBuilding
    DROP CONSTRAINT Test_IndustrialBuilding_FK;

-- -------------------------------------------------------------------- 
-- Test_building 
-- -------------------------------------------------------------------- 
ALTER TABLE Test_building
    DROP CONSTRAINT Test_building_FK;

-- ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ 
-- ***********************************  Drop tables  ************************************** 
-- ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ 
-- -------------------------------------------------------------------- 
-- Test_BuildingUnit 
-- -------------------------------------------------------------------- 
DROP TABLE Test_BuildingUnit;

-- -------------------------------------------------------------------- 
-- Test_BuildingUnit_to_address 
-- -------------------------------------------------------------------- 
DROP TABLE Test_BuildingUnit_to_address;

-- -------------------------------------------------------------------- 
-- Test_EnergyPerformanceCertific 
-- -------------------------------------------------------------------- 
DROP TABLE Test_EnergyPerformanceCertific;

-- -------------------------------------------------------------------- 
-- Test_Facilities 
-- -------------------------------------------------------------------- 
DROP TABLE Test_Facilities;

-- -------------------------------------------------------------------- 
-- Test_IndustrialBuilding 
-- -------------------------------------------------------------------- 
DROP TABLE Test_IndustrialBuilding;

-- -------------------------------------------------------------------- 
-- Test_building 
-- -------------------------------------------------------------------- 
DROP TABLE Test_building;

-- ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ 
-- *********************************  Drop Sequences  ************************************* 
-- ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ 

DROP SEQUENCE Test_EnergyPerformanceCert_SEQ;

PURGE RECYCLEBIN;
