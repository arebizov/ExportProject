package ru.projectMS.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

public class ProjectServer {


    private int id;

    private String projectName;
    private String corpCmr;

    private String corpObj;

    public ProjectServer() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getCorpCmr() {
        return corpCmr;
    }

    public void setCorpCmr(String corpCmr) {
        this.corpCmr = corpCmr;
    }

    public String getCorpObj() {
        return corpObj;
    }



    public void setCorpObj(String corpObj) {
        this.corpObj = corpObj;
    }

    public ProjectServer(int taskId, String corpCMR, String corpObj, String projectName) {

       this.id = taskId;
       this.corpCmr = corpCMR;
       this.corpObj = corpObj;
       this.projectName = projectName;


    }


}
