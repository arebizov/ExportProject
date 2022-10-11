package ru.projectMS.model;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;


@Entity
@Table(name = "Project")
public class ProjectData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "resource_name")
    private String resourceName;

    @Column(name = "task_id")
    private int taskID;

    @Column(name = "task_name")
    private String taskName;

    @Column(name = "date_assig")
    private Date dateAssig;

    @Column(name = "val")
    private float val;

    @Column(name = "task_guid")
    private String taskGuid;

    @Column(name = "monday")
    private Date monday;

    @Column(name = "resource_type")
    private String resourceType;

    @Column(name = "builder")
    private String builder;

    @Column(name = "type_work")
    private String typeWork;

    @Column(name = "act_finish")
    private Date actFinish;

    @Column(name = "sum_task")
    private String sumTask;

    @Column(name = "start")
    private Date start;

    @Column(name = "finish")
    private Date finish;

    @Column(name = "project_name")
    private String projectName;

    @Column(name = "material_label")
    private String materialLabel;

    @Column(name = "modified_date")
    private Timestamp modifiedDate;

    @Column(name = "type")
    private String types;

    public ProjectData() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public int getTaskID() {
        return taskID;
    }

    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }

    public String getTaskName() {
        return taskName;
    }



    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public Date getDateAssig() {
        return dateAssig;
    }

    public void setDateAssig(Date dateAssig) {
        this.dateAssig = dateAssig;
    }

    public float getVal() {
        return val;
    }

    public void setVal(float val) {
        this.val = val;
    }

    public String getTaskGuid() {
        return taskGuid;
    }

    public void setTaskGuid(String taskGuid) {
        this.taskGuid = taskGuid;
    }

    public Date getMonday() {
        return monday;
    }

    public void setMonday(Date monday) {
        this.monday = monday;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public String getBuilder() {
        return builder;
    }

    public void setBuilder(String builder) {
        this.builder = builder;
    }

    public String getTypeWork() {
        return typeWork;
    }

    public void setTypeWork(String typeWork) {
        this.typeWork = typeWork;
    }

    public String getTypes() {
        return types;
    }

    public void setTypes(String types) {
        this.types = types;
    }

    public Date getActFinish() {
        return actFinish;
    }

    public void setActFinish(Date actFinish) {
        this.actFinish = actFinish;
    }

    public String getSumTask() {
        return sumTask;
    }

    public void setSumTask(String sumTask) {
        this.sumTask = sumTask;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getFinish() {
        return finish;
    }

    public void setFinish(Date finish) {
        this.finish = finish;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getMaterialLabel() {
        return materialLabel;
    }

    public void setMaterialLabel(String materialLabel) {
        this.materialLabel = materialLabel;
    }

    public Timestamp getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Timestamp modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public ProjectData(String resourceName, int taskID, String taskName, Date dateAssig, float val, String taskGuid, Date monday, String resourceType, String builder, String typeWork, Date actFinish, String sumTask, Date start, Date finish, String projectName, String materialLabel, Timestamp modifiedDate, String types) {
        this.resourceName = resourceName;
        this.taskID = taskID;
        this.taskName = taskName;
        this.dateAssig = dateAssig;
        this.val = val;
        this.taskGuid = taskGuid;
        this.monday = monday;
        this.resourceType = resourceType;
        this.builder = builder;
        this.typeWork = typeWork;
        this.actFinish = actFinish;
        this.sumTask = sumTask;
        this.start = start;
        this.finish = finish;
        this.projectName = projectName;
        this.materialLabel = materialLabel;
        this.modifiedDate = modifiedDate;
        this.types = types;

    }

    @Override
    public String toString() {
        return "ProjectData{" +
                "id=" + id +
                ", resourceName='" + resourceName + '\'' +
                ", taskID=" + taskID +
                ", taskName='" + taskName + '\'' +
                ", dateAssig=" + dateAssig +
                ", val=" + val +
                ", taskGuid='" + taskGuid + '\'' +
                ", monday=" + monday +
                ", resourceType='" + resourceType + '\'' +
                ", builder='" + builder + '\'' +
                ", typeWork='" + typeWork + '\'' +
                ", actFinish=" + actFinish +
                ", sumTask='" + sumTask + '\'' +
                ", start=" + start +
                ", finish=" + finish +
                ", projectName='" + projectName + '\'' +
                ", materialLabel='" + materialLabel + '\'' +
                ", modifiedDate=" + modifiedDate +
                ", types='" + types + '\'' +
                '}';
    }
}
