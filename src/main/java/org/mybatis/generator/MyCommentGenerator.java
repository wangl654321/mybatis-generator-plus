package org.mybatis.generator;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.mybatis.generator.api.CommentGenerator;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.config.PropertyRegistry;
import org.mybatis.generator.config.TableConfiguration;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import static org.mybatis.generator.internal.util.StringUtility.isTrue;

/***
 *
 *
 * 描    述: mybatis generator 自定义comment生成器.
 *
 * 创 建 者:  @author wl
 * 创建时间:  2019/2/13 15:50
 * 创建描述: 
 *
 * 修 改 者: 
 * 修改时间: 
 * 修改描述: 
 *
 * 审 核 者: 
 * 审核时间: 
 * 审核描述: 
 *
 */
public class MyCommentGenerator implements CommentGenerator {

    private static final Logger logger = LogManager.getLogger(MyCommentGenerator.class);

    /**
     * properties配置文件
     */
    private Properties properties;

    /**
     * properties配置文件
     */
    private Properties systemPro;

    /**
     * 父类时间
     */
    private boolean suppressDate;

    /**
     * 父类所有注释
     */
    private boolean suppressAllComments;

    /**
     * 当前时间
     */
    private String currentDateStr;

    public MyCommentGenerator() {
        properties = new Properties();
        systemPro = System.getProperties();
        suppressDate = false;
        suppressAllComments = false;
        currentDateStr = (new SimpleDateFormat("yyyy-MM-dd")).format(new Date());
    }

    /**
     * Java类的类注释
     *
     * @param innerClass
     * @param introspectedTable
     */
    @Override
    public void addClassComment(InnerClass innerClass, IntrospectedTable introspectedTable) {
        if (suppressAllComments) {
            return;
        }

        StringBuilder sb = new StringBuilder();
        innerClass.addJavaDocLine("/**");
        sb.append(" * ");
        sb.append(introspectedTable.getFullyQualifiedTable());
        sb.append(" ");
        sb.append(getDateString());
        innerClass.addJavaDocLine(sb.toString().replace("\n", " "));
        innerClass.addJavaDocLine(" */");
    }

    /**
     * 为类添加注释
     *
     * @param innerClass
     * @param introspectedTable
     * @param markAsDoNotDelete
     */
    @Override
    public void addClassComment(InnerClass innerClass, IntrospectedTable introspectedTable, boolean markAsDoNotDelete) {
        if (suppressAllComments) {
            return;
        }
        logger.info("为类添加注释");
        StringBuilder sb = new StringBuilder();
        innerClass.addJavaDocLine("/**");
        sb.append(" * ");
        sb.append(" * ");
        sb.append(introspectedTable.getFullyQualifiedTable());
        innerClass.addJavaDocLine(sb.toString().replace("\n", " "));
        sb.setLength(0);
        sb.append(" * @author ");
        sb.append(systemPro.getProperty("user.name"));
        sb.append(" ");
        sb.append(currentDateStr);
        innerClass.addJavaDocLine(" */");
    }


    /**
     * 给生成的XML文件加注释,添加一个合适的注释来警告用户生成元素，以及何时生成元素。
     *
     * @param xmlElement
     */
    @Override
    public void addComment(XmlElement xmlElement) {

    }

    /**
     * 从该配置中的任何属性添加此实例的属性CommentGenerator配置,这个方法将在任何其他方法之前被调用
     *
     * @param properties
     */
    @Override
    public void addConfigurationProperties(Properties properties) {
        this.properties.putAll(properties);
        suppressDate = isTrue(properties.getProperty(PropertyRegistry.COMMENT_GENERATOR_SUPPRESS_DATE));
        suppressAllComments = isTrue(properties.getProperty(PropertyRegistry.COMMENT_GENERATOR_SUPPRESS_ALL_COMMENTS));
    }

    /**
     * 此方法返回格式化的日期字符串以包含在Javadoc标记中和XML注释。 如果您不想要日期，则可以返回null在这些文档元素中
     *
     * @return
     */
    protected String getDateString() {
        String result = null;
        if (!suppressDate) {
            result = currentDateStr;
        }
        return result;
    }


    /**
     * 为枚举添加注释
     *
     * @param innerEnum
     * @param introspectedTable
     */
    @Override
    public void addEnumComment(InnerEnum innerEnum, IntrospectedTable introspectedTable) {
        if (suppressAllComments) {
            return;
        }
        logger.info("为枚举添加注释");
        StringBuilder sb = new StringBuilder();
        innerEnum.addJavaDocLine("/**");
        sb.append(" * ");
        sb.append(introspectedTable.getFullyQualifiedTable());
        innerEnum.addJavaDocLine(sb.toString().replace("\n", " "));
        innerEnum.addJavaDocLine(" */");
    }

    /**
     * Java属性注释
     *
     * @param field
     * @param introspectedTable
     */
    @Override
    public void addFieldComment(Field field, IntrospectedTable introspectedTable) {
        if (suppressAllComments) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        field.addJavaDocLine("/**");
        sb.append(" * ");
        sb.append(introspectedTable.getFullyQualifiedTable());
        field.addJavaDocLine(sb.toString().replace("\n", " "));
        field.addJavaDocLine(" */");
    }

    /**
     * 为字段添加注释
     *
     * @param field
     * @param introspectedTable
     * @param introspectedColumn
     */
    @Override
    public void addFieldComment(Field field, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn) {
        if (suppressAllComments) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        field.addJavaDocLine("/**");
        sb.append(" * ");
        sb.append(introspectedColumn.getRemarks());
        field.addJavaDocLine(sb.toString().replace("\n", " "));
        field.addJavaDocLine(" */");
    }

    /**
     * 为dao层方法添加注释
     *
     * @param method
     * @param introspectedTable
     */
    @Override
    public void addGeneralMethodComment(Method method, IntrospectedTable introspectedTable) {

        if (suppressAllComments) {
            return;
        }
        String toString = "toString";
        String name = method.getName();
        if (toString.equals(name)) {
            return;
        }

        String insert = "insert";
        String delete = "delete";
        String update = "update";
        String select = "select";

        method.addJavaDocLine("/**");
        if (name.contains(insert)) {
            if(name.toLowerCase().contains(select)){
                method.addJavaDocLine(" * 保存带标签");
            }else {
                method.addJavaDocLine(" * 保存不带标签");
            }
        }
        if (name.contains(delete)) {
            method.addJavaDocLine(" * 根据主键删除");
        }
        if (name.contains(update)) {
            if(name.toLowerCase().contains(select)){
                method.addJavaDocLine(" * 更新带标签");
            }else {
                method.addJavaDocLine(" * 更新不带标签");
            }
        }
        if (name.contains(select)) {
            method.addJavaDocLine(" * 根据主键查询");
        }
        method.addJavaDocLine(" * ");
        String param = method.getParameters().get(0).getName();
        method.addJavaDocLine(" * @param " + param);
        String shortName = method.getReturnType().getShortName();
        method.addJavaDocLine(" * @return " + shortName);
        method.addJavaDocLine(" */");
    }

    /**
     * 给Java文件加注释，这个注释是在文件的顶部，也就是package上面。
     *
     * @param compilationUnit
     */
    @Override
    public void addJavaFileComment(CompilationUnit compilationUnit) {

        logger.info(compilationUnit.getType().getShortName() + ".java 方法进入");
    }

    /**
     * 为类添加模型注释
     *
     * @param topLevelClass
     * @param introspectedTable
     */
    @Override
    public void addModelClassComment(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {

        String author = properties.getProperty("author");
        logger.info(topLevelClass.getType().getShortName() + "方法进入");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        topLevelClass.addJavaDocLine("/***");
        topLevelClass.addJavaDocLine(" *");
        topLevelClass.addJavaDocLine(" *");
        TableConfiguration tableConfiguration = introspectedTable.getTableConfiguration();
        String remarks = introspectedTable.getRemarks();
        topLevelClass.addJavaDocLine(" * 描    述: " + tableConfiguration + " " + remarks);
        topLevelClass.addJavaDocLine(" *");
        topLevelClass.addJavaDocLine(" * 创 建 者: @author " + author);
        topLevelClass.addJavaDocLine(" * 创建时间: " + sdf.format(new Date()));
        topLevelClass.addJavaDocLine(" * 创建描述: ");
        topLevelClass.addJavaDocLine(" *");
        topLevelClass.addJavaDocLine(" * 修 改 者: ");
        topLevelClass.addJavaDocLine(" * 修改时间: ");
        topLevelClass.addJavaDocLine(" * 修改描述: ");
        topLevelClass.addJavaDocLine(" *");
        topLevelClass.addJavaDocLine(" * 审 核 者: ");
        topLevelClass.addJavaDocLine(" * 审核时间: ");
        topLevelClass.addJavaDocLine(" * 审核描述: ");
        topLevelClass.addJavaDocLine(" *");
        topLevelClass.addJavaDocLine(" */");
    }

    /**
     * 为调用此方法作为根元素的第一个子节点添加注释
     *
     * @param xmlElement
     */
    @Override
    public void addRootComment(XmlElement xmlElement) {
    }

    /**
     * 给getter方法加注释
     *
     * @param method
     * @param introspectedTable
     * @param introspectedColumn
     */
    @Override
    public void addGetterComment(Method method, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn) {
        if (suppressAllComments) {
            return;
        }
        method.addJavaDocLine("/**");
        StringBuilder sb = new StringBuilder();
        sb.append(" * ");
        sb.append(introspectedColumn.getRemarks());
        method.addJavaDocLine(sb.toString().replace("\n", " "));
        sb.setLength(0);
        sb.append(" * @return ");
        sb.append(introspectedColumn.getActualColumnName());
        sb.append(" ");
        sb.append(introspectedColumn.getRemarks());
        method.addJavaDocLine(sb.toString().replace("\n", " "));
        method.addJavaDocLine(" */");
    }

    /**
     * 给setter方法加注释
     *
     * @param method
     * @param introspectedTable
     * @param introspectedColumn
     */
    @Override
    public void addSetterComment(Method method, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn) {
        if (suppressAllComments) {
            return;
        }
        method.addJavaDocLine("/**");
        StringBuilder sb = new StringBuilder();
        sb.append(" * ");
        sb.append(introspectedColumn.getRemarks());
        method.addJavaDocLine(sb.toString().replace("\n", " "));
        Parameter parm = method.getParameters().get(0);
        sb.setLength(0);
        sb.append(" * @param ");
        sb.append(parm.getName());
        sb.append(" ");
        sb.append(introspectedColumn.getRemarks());
        method.addJavaDocLine(sb.toString().replace("\n", " "));
        method.addJavaDocLine(" */");
    }
}