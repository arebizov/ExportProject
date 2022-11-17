package ru.projectMS.filesProject;

public class Profile {

    public String getSchema() {
        String shema = " pbi_1c.";
        return shema;
    }

    public String getSchemaProjectServer() {
        String shema = " dbo.";
        return shema;
    }

    public String getURLDb() {
        String url="jdbc:sqlserver://VM-MSKSS-DIR02;"+"database=PORTAL_DWH;"+"user=airflow;"+"password=Cegthgfhjkm1@;";
//        String url = "jdbc:sqlserver://localhost:1433;"+"database=master;" + "user=sa;"+"password=Supperpassword1@;";
        return url;
    }

    public String getURLDbProjectServer() {
        String url="jdbc:sqlserver://vm-srv-sqld\\sqld;"+"database=ProjectWebApp;"+"user=bitrix_updater;"+"password=Vflupdater;";
//        String url = "jdbc:sqlserver://localhost:1433;"+"database=master;" + "user=sa;"+"password=Supperpassword1@;";
        return url;
    }

    public String getDataFromMSProject(String projectName) {
        String url = "exec sp_executesql N'WITH OdataSelect AS\n" +
                "(\n" +
                "SELECT\n" +
                "   [MSP_TVF_EpmTask].[TaskParentUID] AS [ИдентификаторРодительскойЗадачи],\n" +
                "   [TaskParentTable].[TaskName] AS [НазваниеРодительскойЗадачи],\n" +
                "   [MSP_TVF_EpmTask].[ProjectUID] AS [ИДПроекта],\n" +
                "   [MSP_TVF_EpmProject].[ProjectName] AS [ИмяПроекта],\n" +
                "   [MSP_TVF_EpmTask].[TaskActualCost] AS [ФактическиеЗатратыНаЗадачу],\n" +
                "   [MSP_TVF_EpmTask].[TaskActualDuration] AS [ФактическаяДлительностьЗадачи],\n" +
                "   [MSP_TVF_EpmTask].[TaskActualFinishDate] AS [ФактическаяДатаОкончанияЗадачи],\n" +
                "   [MSP_TVF_EpmTask].[TaskActualFixedCost] AS [ФактическиеФиксированныеЗатратыНаЗадачу],\n" +
                "   [MSP_TVF_EpmTask].[TaskActualOvertimeCost] AS [ФактическиеЗатратыНаСверхурочныеЗадачи],\n" +
                "   [MSP_TVF_EpmTask].[TaskActualOvertimeWork] AS [ФактическиеСверхурочныеТрудозатратыЗадачи],\n" +
                "   [MSP_TVF_EpmTask].[TaskActualCost] - [MSP_TVF_EpmTask].[TaskActualOvertimeCost] AS [ФактическиеОбычныеЗатратыЗадачи],\n" +
                "   [MSP_TVF_EpmTask].[TaskActualWork] - [MSP_TVF_EpmTask].[TaskActualOvertimeWork] AS [ФактическиеОбычныеТрудозатратыЗадачи],\n" +
                "   [MSP_TVF_EpmTask].[TaskActualStartDate] AS [ФактическаяДатаНачалаЗадачи],\n" +
                "   [MSP_TVF_EpmTask].[TaskActualWork] AS [ФактическиеТрудозатратыЗадачи],\n" +
                "   [MSP_TVF_EpmTask].[TaskACWP] AS [ФСВРЗадачи],\n" +
                "   [MSP_TVF_EpmTask].[TaskBCWP] AS [БСВРЗадачи],\n" +
                "   [MSP_TVF_EpmTask].[TaskBCWS] AS [БСЗРЗадачи],\n" +
                "   [MSP_TVF_EpmTask].[TaskBudgetCost] AS [БюджетныеЗатратыЗадачи],\n" +
                "   [MSP_TVF_EpmTask].[TaskBudgetWork] AS [БюджетныеТрудозатратыЗадачи],\n" +
                "   [MSP_TVF_EpmTask].[TaskClientUniqueId] AS [УникальныйИдентификаторКлиентаЗадачи],\n" +
                "   [MSP_TVF_EpmTask].[TaskCost] AS [ЗатратыНаЗадачу],\n" +
                "   [MSP_TVF_EpmTask].[TaskCostVariance] AS [ОтклонениеПоСтоимостиЗадачи],\n" +
                "   [MSP_TVF_EpmTask].[TaskCPI] AS [ИОСЗадачи],\n" +
                "   [MSP_TVF_EpmTask].[TaskCreatedDate] AS [ДатаСозданияЗадачи],\n" +
                "   [MSP_TVF_EpmTask].[TaskCreatedRevisionCounter] AS [СчетчикСозданныхВерсийЗадачи],\n" +
                "   [MSP_TVF_EpmTask].[TaskCV] AS [ОПСЗадачи],\n" +
                "   CASE [MSP_TVF_EpmTask].[TaskBCWP] WHEN 0 THEN 0 ELSE [MSP_TVF_EpmTask].[TaskCV]/[MSP_TVF_EpmTask].[TaskBCWP] END AS [ООПСЗадачи],\n" +
                "   [MSP_TVF_EpmTask].[TaskDeadline] AS [КрайнийСрокЗадачи],\n" +
                "   [MSP_TVF_EpmTask].[TaskDeliverableFinishDate] AS [ДатаОкончанияПоставкиКонечногоРезультатаЗадачи],\n" +
                "   [MSP_TVF_EpmTask].[TaskDeliverableStartDate] AS [ДатаНачалаПоставкиКонечногоРезультатаЗадачи],\n" +
                "   [MSP_TVF_EpmTask].[TaskDuration] AS [ДлительностьЗадачи],\n" +
                "   [MSP_TVF_EpmTask].[TaskDurationIsEstimated] AS [ОценкаДлительностиЗадачи],\n" +
                "   [MSP_TVF_EpmTask].[TaskDurationString] AS [СтрокаДлительностиЗадачи],\n" +
                "   [MSP_TVF_EpmTask].[TaskDurationVariance] AS [ОтклонениеДлительностиЗадачи],\n" +
                "   [MSP_TVF_EpmTask].[TaskEAC] AS [ПОПЗЗадачи],\n" +
                "   [MSP_TVF_EpmTask].[TaskEarlyFinish] AS [РаннееОкончаниеЗадачи],\n" +
                "   [MSP_TVF_EpmTask].[TaskEarlyStart] AS [РанееНачалоЗадачи],\n" +
                "   [MSP_TVF_EpmTask].[TaskFinishDate] AS [ДатаОкончанияЗадачи],\n" +
                "   [MSP_TVF_EpmTask].[TaskFinishDateString] AS [СтрокаДатыОкончанияЗадачи],\n" +
                "   [MSP_TVF_EpmTask].[TaskFinishVariance] AS [ОтклонениеОкончанияЗадачи],\n" +
                "   [MSP_TVF_EpmTask].[TaskFixedCost] AS [ФиксированныеЗатратыЗадачи],\n" +
                "   [MSP_TVF_EpmTask].[FixedCostAssignmentUID] AS [ИдентификаторНазначенияФиксированныхЗатратНаЗадачу],\n" +
                "   [MSP_TVF_EpmTask].[TaskFreeSlack] AS [СвободныйВременнойРезервЗадачи],\n" +
                "   [MSP_TVF_EpmTask].[TaskHyperLinkAddress] AS [АдресГиперссылкиЗадачи],\n" +
                "   [MSP_TVF_EpmTask].[TaskHyperLinkFriendlyName] AS [ПонятноеИмяГиперссылкиЗадачи],\n" +
                "   [MSP_TVF_EpmTask].[TaskHyperLinkSubAddress] AS [СубадрессГиперссылкиЗадачи],\n" +
                "   [MSP_TVF_EpmTask].[TaskUID] AS [ИдентификаторЗадачи],\n" +
                "   [MSP_TVF_EpmTask].[TaskIgnoresResourceCalendar] AS [НеУчитыватьКалендарьРесурсаЗадачи],\n" +
                "   [MSP_TVF_EpmTask].[TaskIndex] AS [ИндексЗадачи],\n" +
                "   [MSP_TVF_EpmTask].[TaskIsActive] AS [ЗадачаЯвляетсяАктивной],\n" +
                "   [MSP_TVF_EpmTask].[TaskIsCritical] AS [КритическаяЗадача],\n" +
                "   [MSP_TVF_EpmTask].[TaskIsEffortDriven] AS [ЗадачаСФиксированнымОбъемомРабот],\n" +
                "   [MSP_TVF_EpmTask].[TaskIsExternal] AS [ЗадачаЯвляетсяВнешней],\n" +
                "   [MSP_TVF_EpmTask].[TaskIsManuallyScheduled] AS [ЗадачаПланируетсяВручную],\n" +
                "   [MSP_TVF_EpmTask].[TaskIsMarked] AS [ЗадачаПомечена],\n" +
                "   [MSP_TVF_EpmTask].[TaskIsMilestone] AS [ЗадачаЯвляетсяВехой],\n" +
                "   [MSP_TVF_EpmTask].[TaskIsOverallocated] AS [ЗадачаСПревышениемДоступности],\n" +
                "   [MSP_TVF_EpmTask].[TaskIsProjectSummary] AS [ЗадачаЯвляетсяСуммарнойДляПроекта],\n" +
                "   [MSP_TVF_EpmTask].[TaskIsRecurring] AS [ПовторяющаясяЗадача],\n" +
                "   [MSP_TVF_EpmTask].[TaskIsSummary] AS [ЗадачаЯвляетсяСуммарной],\n" +
                "   [MSP_TVF_EpmTask].[TaskLateFinish] AS [ПозднееОкончаниеЗадачи],\n" +
                "   [MSP_TVF_EpmTask].[TaskLateStart] AS [ПозднееНачалоЗадачи],\n" +
                "   [MSP_TVF_EpmTask].[TaskLevelingDelay] AS [ВыравнивающаяЗадержкаЗадачи],\n" +
                "   [MSP_TVF_EpmTask].[TaskModifiedDate] AS [ДатаИзмененияЗадачи],\n" +
                "   [MSP_TVF_EpmTask].[TaskModifiedRevisionCounter] AS [СчетчикИзмененныхВерсийЗадачи],\n" +
                "   [MSP_TVF_EpmTask].[TaskName] AS [НазваниеЗадачи],\n" +
                "   [MSP_TVF_EpmTask].[TaskOutlineLevel] AS [УровеньСтруктурыЗадачи],\n" +
                "   [MSP_TVF_EpmTask].[TaskOutlineNumber] AS [НомерЗадачиВСтруктуре],\n" +
                "   [MSP_TVF_EpmTask].[TaskOvertimeCost] AS [ЗатратыНаСверхурочныеЗадачи],\n" +
                "   [MSP_TVF_EpmTask].[TaskOvertimeWork] AS [СверхурочныеТрудозатратыЗадачи],\n" +
                "   [MSP_TVF_EpmTask].[TaskPercentCompleted] AS [ПроцентЗавершенияЗадачи],\n" +
                "   [MSP_TVF_EpmTask].[TaskPercentWorkCompleted] AS [ПроцентЗавершенияЗадачиПоТрудозатратам],\n" +
                "   [MSP_TVF_EpmTask].[TaskPhysicalPercentCompleted] AS [ФизическийПроцентЗавершенияЗадачи],\n" +
                "   [MSP_TVF_EpmTask].[TaskPriority] AS [ПриоритетЗадачи],\n" +
                "   [MSP_TVF_EpmTask].[TaskCost] - [MSP_TVF_EpmTask].[TaskOvertimeCost] AS [ОбычныеЗатратыЗадачи],\n" +
                "   [MSP_TVF_EpmTask].[TaskWork] - [MSP_TVF_EpmTask].[TaskOvertimeWork] AS [ОбычныеТрудозатратыЗадачи],\n" +
                "   [MSP_TVF_EpmTask].[TaskCost] - [MSP_TVF_EpmTask].[TaskActualCost] AS [ОставшиесяЗатратыЗадачи],\n" +
                "   [MSP_TVF_EpmTask].[TaskDuration] - [MSP_TVF_EpmTask].[TaskActualDuration] AS [ОставшаясяДлительностьЗадачи],\n" +
                "   [MSP_TVF_EpmTask].[TaskOvertimeCost] - [MSP_TVF_EpmTask].[TaskActualOvertimeCost] AS [ОставшиесяСверхурочныеЗатратыНаЗадачу],\n" +
                "   [MSP_TVF_EpmTask].[TaskOvertimeWork] - [MSP_TVF_EpmTask].[TaskActualOvertimeWork] AS [ОставшиесяСверхурочныеТрудозатратыЗадачи],\n" +
                "   ([MSP_TVF_EpmTask].[TaskCost] - [MSP_TVF_EpmTask].[TaskOvertimeCost]) - ([MSP_TVF_EpmTask].[TaskActualCost] - [MSP_TVF_EpmTask].[TaskActualOvertimeCost]) AS [ОставшиесяОбычныеЗатратыНаЗадачу],\n" +
                "   ([MSP_TVF_EpmTask].[TaskWork] - [MSP_TVF_EpmTask].[TaskOvertimeWork]) - ([MSP_TVF_EpmTask].[TaskActualWork] - [MSP_TVF_EpmTask].[TaskActualOvertimeWork]) AS [ОставшиесяОбычныеТрудозатратыЗадачи],\n" +
                "   [MSP_TVF_EpmTask].[TaskWork] - [MSP_TVF_EpmTask].[TaskActualWork] AS [ОставшиесяТрудозатратыЗадачи],\n" +
                "   [MSP_TVF_EpmTask].[TaskResourcePlanWork] AS [ТрудозатратыПланаРесурсовЗадачи],\n" +
                "   [MSP_TVF_EpmTask].[TaskSPI] AS [ИОКПЗадачи],\n" +
                "   [MSP_TVF_EpmTask].[TaskStartDate] AS [ДатаНачалаЗадачи],\n" +
                "   [MSP_TVF_EpmTask].[TaskStartDateString] AS [СтрокаДатыНачалаЗадачи],\n" +
                "   [MSP_TVF_EpmTask].[TaskStartVariance] AS [ОтклонениеНачалаЗадачи],\n" +
                "   [MSP_TVF_EpmTask].[TaskStatusManagerUID] AS [TaskStatusManagerUID],\n" +
                "   [MSP_TVF_EpmTask].[TaskSV] AS [ОКПЗадачи],\n" +
                "   CASE [MSP_TVF_EpmTask].[TaskBCWS] WHEN 0 THEN 0 ELSE [MSP_TVF_EpmTask].[TaskSV]/[MSP_TVF_EpmTask].[TaskBCWS] END AS [ООКПЗадачи],\n" +
                "   [MSP_TVF_EpmTask].[TaskTCPI] AS [ПЭВЗадачи],\n" +
                "   [MSP_TVF_EpmTask].[TaskTotalSlack] AS [ОбщийВременнойРезерв],\n" +
                "   [MSP_TVF_EpmTask].[TaskVAC] AS [ОПЗЗадачи],\n" +
                "   CAST([MSP_TVF_EpmTask].[TaskWBS] AS NVARCHAR(MAX)) AS [СДРЗадачи],\n" +
                "   [MSP_TVF_EpmTask].[TaskWork] AS [ТрудозатратыЗадачи],\n" +
                "   [MSP_TVF_EpmTask].[TaskWorkVariance] AS [Отклонение_трудозатраты_задачи],\n" +
                "   CAST([MSP_EpmLookupTable0000e8d9-65f1-4769-9bd2-819d38036fcc].[MemberValue] as NVARCHAR(MAX)) AS [Работоспособность],\n" +
                "   CAST([MSP_TVF_EpmCPTaskVar0].[CFVal0] as [Bit]) AS [Состояниеотметки],\n" +
                "   [MSP_TVF_EpmCPTaskStr0].[CFVal0] AS [TEST_INT],\n" +
                "   CAST([MSP_TVF_EpmCPTaskVar0].[CFVal1] as [Bit]) AS [КСП],\n" +
                "   CAST([MSP_EpmLookupTable6d130d1c-5ee9-eb11-8db6-005056bd9183].[MemberFullValue] as NVARCHAR(MAX)) AS [Типработы],\n" +
                "   CAST([MSP_EpmLookupTable529eccfc-10f9-eb11-8db6-005056bd9183].[MemberFullValue] as NVARCHAR(MAX)) AS [ОтчётвPowerBI],\n" +
                "   CAST([MSP_EpmLookupTable60baa6fb-89fe-eb11-8db8-005056bd9183].[MemberValue] as NVARCHAR(MAX)) AS [ТипСМР],\n" +
                "   CAST([MSP_EpmLookupTable643cd319-bf00-ec11-8db8-005056bd9183].[MemberValue] as NVARCHAR(MAX)) AS [Этаж],\n" +
                "   CAST([MSP_EpmLookupTable3756e9f1-1301-ec11-8428-5065f348bdee].[MemberFullValue] as NVARCHAR(MAX)) AS [Кодработы],\n" +
                "   CAST([MSP_EpmLookupTablec365c98e-8505-ec11-8db8-005056bd9183].[MemberValue] as NVARCHAR(MAX)) AS [Флаг],\n" +
                "   CAST([MSP_TVF_EpmCPTaskVar0].[CFVal2] as [DateTime]) AS [Скорректированноеокончание],\n" +
                "   CAST([MSP_TVF_EpmCPTaskVar0].[CFVal4] as [Bit]) AS [КритическаязадачаКСП],\n" +
                "   CAST([MSP_TVF_EpmCPTaskVar0].[CFVal3] as [DateTime]) AS [Расчётноеокончаниепредыдущее],\n" +
                "   CAST([MSP_EpmLookupTablecc216a24-0716-ec11-8db9-005056bd9183].[MemberFullValue] as NVARCHAR(MAX)) AS [КонтрольныесрокиДГП],\n" +
                "   [MSP_TVF_EpmCPTaskStr0].[CFVal1] AS [ОтклонениеокончаниядляBi],\n" +
                "   CAST([MSP_EpmLookupTable7ec7794f-53a4-ec11-8dbc-005056bd9183].[MemberValue] as NVARCHAR(MAX)) AS [ОбъёмдляPowerBi],\n" +
                "   CAST([MSP_TVF_EpmCPTaskVar0].[CFVal5] as [Bit]) AS [Гант],\n" +
                "   [MSP_TVF_EpmCPTaskStr0].[CFVal2] AS [Объектстроительства],\n" +
                "   CAST([MSP_EpmLookupTableb8f32e90-b6d6-ec11-8dbd-005056bd9183].[MemberValue] as NVARCHAR(MAX)) AS [Объектреализации],\n" +
                "   CAST([MSP_EpmLookupTable144103a8-17e7-ec11-8dbd-005056bd9183].[MemberValue] as NVARCHAR(MAX)) AS [КонтрактацияЗакупки],\n" +
                "   ROW_NUMBER() OVER (ORDER BY [MSP_TVF_EpmTask].[ProjectUID] ASC, [MSP_TVF_EpmTask].[TaskUID] ASC) as RowNumber\n" +
                "FROM [pjrep].[MSP_TVF_EpmTask](@siteId)\n" +
                "LEFT JOIN [pjrep].[MSP_TVF_EpmTask](@siteId) AS [TaskParentTable] ON [MSP_TVF_EpmTask].[TaskParentUID]=[TaskParentTable].[TaskUID]\n" +
                "LEFT JOIN [pjrep].[MSP_TVF_EpmProject](@siteId) ON [MSP_TVF_EpmTask].[ProjectUID]=[MSP_TVF_EpmProject].[ProjectUID]\n" +
                "LEFT JOIN [pjrep].[MSP_TVF_EpmCPTaskUid0](@siteId) ON [MSP_TVF_EpmTask].[TaskUID] = [MSP_TVF_EpmCPTaskUid0].[EntityUID]\n" +
                "LEFT JOIN [pjrep].[MSP_TVF_EpmLookupTable](@siteId) AS [MSP_EpmLookupTable0000e8d9-65f1-4769-9bd2-819d38036fcc] ON [MSP_EpmLookupTable0000e8d9-65f1-4769-9bd2-819d38036fcc].[LookupTableUID] = ''0000a4aa-160e-499a-905f-d498472dfb35'' AND [MSP_EpmLookupTable0000e8d9-65f1-4769-9bd2-819d38036fcc].[MemberUID] = [MSP_TVF_EpmCPTaskUid0].[CFVal0]\n" +
                "LEFT JOIN [pjrep].[MSP_TVF_EpmCPTaskVar0](@siteId) ON [MSP_TVF_EpmTask].[TaskUID] = [MSP_TVF_EpmCPTaskVar0].[EntityUID]\n" +
                "LEFT JOIN [pjrep].[MSP_TVF_EpmCPTaskStr0](@siteId) ON [MSP_TVF_EpmTask].[TaskUID] = [MSP_TVF_EpmCPTaskStr0].[EntityUID]\n" +
                "LEFT JOIN [pjrep].[MSP_TVF_EpmLookupTable](@siteId) AS [MSP_EpmLookupTable6d130d1c-5ee9-eb11-8db6-005056bd9183] ON [MSP_EpmLookupTable6d130d1c-5ee9-eb11-8db6-005056bd9183].[LookupTableUID] = ''6d6fb4f5-5de9-eb11-8db6-005056bd9183'' AND [MSP_EpmLookupTable6d130d1c-5ee9-eb11-8db6-005056bd9183].[MemberUID] = [MSP_TVF_EpmCPTaskUid0].[CFVal2]\n" +
                "LEFT JOIN [pjrep].[MSP_TVF_EpmLookupTable](@siteId) AS [MSP_EpmLookupTable529eccfc-10f9-eb11-8db6-005056bd9183] ON [MSP_EpmLookupTable529eccfc-10f9-eb11-8db6-005056bd9183].[LookupTableUID] = ''2832bc9e-0ff9-eb11-8db6-005056bd9183'' AND [MSP_EpmLookupTable529eccfc-10f9-eb11-8db6-005056bd9183].[MemberUID] = [MSP_TVF_EpmCPTaskUid0].[CFVal3]\n" +
                "LEFT JOIN [pjrep].[MSP_TVF_EpmLookupTable](@siteId) AS [MSP_EpmLookupTable60baa6fb-89fe-eb11-8db8-005056bd9183] ON [MSP_EpmLookupTable60baa6fb-89fe-eb11-8db8-005056bd9183].[LookupTableUID] = ''bb1c5e75-89fe-eb11-8db8-005056bd9183'' AND [MSP_EpmLookupTable60baa6fb-89fe-eb11-8db8-005056bd9183].[MemberUID] = [MSP_TVF_EpmCPTaskUid0].[CFVal4]\n" +
                "LEFT JOIN [pjrep].[MSP_TVF_EpmLookupTable](@siteId) AS [MSP_EpmLookupTable643cd319-bf00-ec11-8db8-005056bd9183] ON [MSP_EpmLookupTable643cd319-bf00-ec11-8db8-005056bd9183].[LookupTableUID] = ''2c48030d-bf00-ec11-8db8-005056bd9183'' AND [MSP_EpmLookupTable643cd319-bf00-ec11-8db8-005056bd9183].[MemberUID] = [MSP_TVF_EpmCPTaskUid0].[CFVal5]\n" +
                "LEFT JOIN [pjrep].[MSP_TVF_EpmLookupTable](@siteId) AS [MSP_EpmLookupTable3756e9f1-1301-ec11-8428-5065f348bdee] ON [MSP_EpmLookupTable3756e9f1-1301-ec11-8428-5065f348bdee].[LookupTableUID] = ''badcedd9-1301-ec11-8428-5065f348bdee'' AND [MSP_EpmLookupTable3756e9f1-1301-ec11-8428-5065f348bdee].[MemberUID] = [MSP_TVF_EpmCPTaskUid0].[CFVal1]\n" +
                "LEFT JOIN [pjrep].[MSP_TVF_EpmLookupTable](@siteId) AS [MSP_EpmLookupTablec365c98e-8505-ec11-8db8-005056bd9183] ON [MSP_EpmLookupTablec365c98e-8505-ec11-8db8-005056bd9183].[LookupTableUID] = ''27f5304e-8505-ec11-8db8-005056bd9183'' AND [MSP_EpmLookupTablec365c98e-8505-ec11-8db8-005056bd9183].[MemberUID] = [MSP_TVF_EpmCPTaskUid0].[CFVal6]\n" +
                "LEFT JOIN [pjrep].[MSP_TVF_EpmLookupTable](@siteId) AS [MSP_EpmLookupTablecc216a24-0716-ec11-8db9-005056bd9183] ON [MSP_EpmLookupTablecc216a24-0716-ec11-8db9-005056bd9183].[LookupTableUID] = ''df9b28ac-fa15-ec11-8db9-005056bd9183'' AND [MSP_EpmLookupTablecc216a24-0716-ec11-8db9-005056bd9183].[MemberUID] = [MSP_TVF_EpmCPTaskUid0].[CFVal7]\n" +
                "LEFT JOIN [pjrep].[MSP_TVF_EpmLookupTable](@siteId) AS [MSP_EpmLookupTable7ec7794f-53a4-ec11-8dbc-005056bd9183] ON [MSP_EpmLookupTable7ec7794f-53a4-ec11-8dbc-005056bd9183].[LookupTableUID] = ''bb1c5e75-89fe-eb11-8db8-005056bd9183'' AND [MSP_EpmLookupTable7ec7794f-53a4-ec11-8dbc-005056bd9183].[MemberUID] = [MSP_TVF_EpmCPTaskUid0].[CFVal8]\n" +
                "LEFT JOIN [pjrep].[MSP_TVF_EpmLookupTable](@siteId) AS [MSP_EpmLookupTableb8f32e90-b6d6-ec11-8dbd-005056bd9183] ON [MSP_EpmLookupTableb8f32e90-b6d6-ec11-8dbd-005056bd9183].[LookupTableUID] = ''f8cfd45b-b5d6-ec11-8dbd-005056bd9183'' AND [MSP_EpmLookupTableb8f32e90-b6d6-ec11-8dbd-005056bd9183].[MemberUID] = [MSP_TVF_EpmCPTaskUid0].[CFVal9]\n" +
                "LEFT JOIN [pjrep].[MSP_TVF_EpmLookupTable](@siteId) AS [MSP_EpmLookupTable144103a8-17e7-ec11-8dbd-005056bd9183] ON [MSP_EpmLookupTable144103a8-17e7-ec11-8dbd-005056bd9183].[LookupTableUID] = ''fb1d106c-17e7-ec11-8dbd-005056bd9183'' AND [MSP_EpmLookupTable144103a8-17e7-ec11-8dbd-005056bd9183].[MemberUID] = [MSP_TVF_EpmCPTaskUid0].[CFVal10]\n" +
                "\n" +
                ")\n" +
                "SELECT   [ИмяПроекта],[ИндексЗадачи] task_id, [ТипСМР] corp_cmr, \n" +
                "[Объектреализации] corp_obj\n" +
                "\n" +
                "FROM OdataSelect where [ИмяПроекта] = N''" + projectName +"''  \n"+
                " ORDER BY RowNumber ASC\n" +
                "',N'@siteId uniqueidentifier',@siteId='B9DA6534-A01C-40B1-B195-201FD4263174'\n";
        return url;
    }

    public String getDataFromLocalProject(String projectName) {
        String url = "select project_name, task_id, corp_cmr, corp_obj  from PBI_1C.project_server where project_name ='"+projectName+"'";

        return url;
    }
}
