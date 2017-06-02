-- ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ 
-- *********************************  Disable Versioning  *********************************** 
-- ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ 

exec DBMS_WM.DisableVersioning('test_BuildingU_to_address,test_BuildingUnit,test_EnergyPerformanceCer,test_Facilities,test_IndustrialBuilding,test_IndustrialBuildingPa,test_IndustrialBuildingRo,test_OtherConstruction,test_Other_to_thema_surfa,test_building,',true, true);
