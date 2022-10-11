package ru.projectMS.model;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@Table(name = "assignment")
public class Assignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "start")
    private Date start;

    @Column(name = "val")
    private float val;

    @Column(name = "finish")
    private Date finish;

    @Column(name = "types")
    private String types;

    @Column(name = "project")
    private String project;

    @Column(name = "resource_name")
    private String resourceName;

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    @Column(name ="task_name")
    private String taskName;


    @Column(name = "period")
    private int period;

    @Column(name = "task_id")
    private int taskID;

    @Column(name = "resource_type")
    private String resourceType;

    @Column(name = "actual_finish")
    private Date actualFinish;

    @Column(name = "material_label")
    private String materialLabel;


    @Column(name = "modified")
    private Timestamp modified;

    @Column(name = "builder")
    private String builder;

    @Column(name = "type_work")
    private String typeWork;

    public Assignment() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getTypes() {
        return types;
    }

    public void setTypes(String types) {
        this.types = types;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public int getTaskID() {
        return taskID;
    }

    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public Date getActualFinish() {
        return actualFinish;
    }

    public void setActualFinish(Date actualFinish) {
        this.actualFinish = actualFinish;
    }

    public String getMaterialLabel() {
        return materialLabel;
    }

    public void setMaterialLabel(String materialLabel) {
        this.materialLabel = materialLabel;
    }

    public Timestamp getModified() {
        return modified;
    }

    public void setModified(Timestamp modified) {
        this.modified = modified;
    }

    public float getVal() {
        return val;
    }

    public void setVal(float val) {
        this.val = val;
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

    public Assignment(Date start, Date finish, float val,  String types,  String taskName, String project, String resourceName, int period, int taskID, String resourceType, String builder, String typeWork, Date actualFinish, String materialLabel, Timestamp modified) {
        this.start = start;
        this.val = val;
        this.finish = finish;
        this.types = types;
        this.project = project;
        this.resourceName = resourceName;
        this.period = period;
        this.taskID = taskID;
        this.resourceType = resourceType;
        this.actualFinish = actualFinish;
        this.materialLabel = materialLabel;
        this.modified = modified;
        this.taskName = taskName;
        this.builder = builder;
        this.typeWork = typeWork;
    }

    @Override
    public String toString() {
        return "Assignment{" +
                "id=" + id +
                ", start=" + start +
                ", val=" + val +
                ", finish=" + finish +
                ", types='" + types + '\'' +
                ", project='" + project + '\'' +
                ", resourceName='" + resourceName + '\'' +
                ", taskName='" + taskName + '\'' +
                ", period=" + period +
                ", taskID=" + taskID +
                ", resourceType='" + resourceType + '\'' +
                ", actualFinish=" + actualFinish +
                ", materialLabel='" + materialLabel + '\'' +
                ", modified=" + modified +
                ", builder='" + builder + '\'' +
                ", typeWork='" + typeWork + '\'' +
                '}';
    }
}
