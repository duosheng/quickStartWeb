package com.soouya.common.util;

import com.google.common.base.Joiner;
import com.soouya.common.model.Tag;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by xuyuli on 2016/6/12.
 */
public class TagUtil {

    public static final String TAG_SPLIT = "_";

    public static ArrayList getTags(String source) {
        String[] initTag=source.split(",");
        ArrayList<Tag> result=new ArrayList<Tag>();
        for(int i=0;i<initTag.length;i++){
            String name=initTag[i];
            if(!name.contains(TAG_SPLIT)){//1级
                Tag tag=new Tag();
                String[] n=name.split(" ");
                if(n.length==0)
                    continue;
                if(n.length>1){
                    tag.setIcon(n[1]);
                }
                tag.setId(n[0]);
                if(n[0].equals("全部"))
                    tag.setType(1);
                else
                    tag.setType(0);
                tag.setName(n[0]);
                ArrayList child=getChildren(n[0],source);
                tag.setValue(child);
                result.add(tag);
            }
        }
        return result;
    }

    public static ArrayList<Tag> getChildren(String pid, String source){
        if(pid.contains(" ")){//remove icon
            pid=pid.split(" ")[0];
        }
        String[] initTag=source.split(",");
        ArrayList<Tag> list=new ArrayList<Tag>();
        for(int i=0;i<initTag.length;i++){
            String name=initTag[i];
            if(name.startsWith(pid+TAG_SPLIT)){
                String value=name.substring(name.indexOf(pid+TAG_SPLIT)+pid.length()+1);
                if(value.length()!=0 && !value.contains(TAG_SPLIT)){
                    Tag tag=new Tag();
                    //Map tag=new HashMap();
                    if(name.contains(" ")){
                        name=name.split(" ")[0];
                    }
                    //System.out.println("name:"+name);
                    String[] v=value.split(" ");
                    tag.setName(v[0]);
                    tag.setId(name);
                    String[] names=name.split(TAG_SPLIT);
                    tag.setType(0);
                    if(names.length==3){
                        if(names[2].equals("所在地区") || names[2].equals("接单状态") || names[2].equals("采购商")){
                            tag.setType(2);
                        }
                    }
                    if(names.length==4){
                        if(names[3].equals("全部")){
                            tag.setType(1);
                        }
                    }
                    if(names.length==2){
                        if(names[1].equals("全部")){
                            tag.setType(1);
                        }
                    }
                    if(v.length>1){
                        tag.setIcon(v[1]);
                    }
                    ArrayList<Tag> child=getChildren(name,source);
                    tag.setValue(child);
                    list.add(tag);
                }else{//has children,
                }
            }
        }
        return list;
    }

    /**
     * 类别+值
     *
     * @param tags
     * @return
     */
    public static String getTitle(String tags) {
        if (tags == null || tags.equals("")) {
            return "";
        }
        String[] tt = tags.split(",");
        String category = null;
        // String category2=null;
        String value = "";
        for (String t : tt) {
            String[] nn = t.split(TAG_SPLIT);// 面料_针织_成分_涤纶
            if (nn.length == 4) {
                if (category == null) {
                    category = nn[1];
                }
                value = value + nn[3] + "、";
            }
        }
        if (category != null) {
            if (!value.equals("")) {
                value = value.substring(0, value.length() - 1);
            }
            value = category + "、" + value;
//            value = value.length() > 10 ? value.substring(0, 10) + "..." : value;
            return value;
        } else {
            return "";
        }
    }

    /**
     * 获取最后1级标签值
     *
     * @param arg
     * @return
     */
    public static String getTagValue(String arg) {
        if (arg == null || arg.equals("")) {
            return "";
        }
        String[] tags = arg.split(",");
        String value = "";
        for (String tag : tags) {
            int i = tag.lastIndexOf(TAG_SPLIT);
            if (i >= 0) {
                value += tag.substring(i + 1) + ",";
            } else {
                value += tag + ",";
            }
        }
        return value;
    }

    /**
     * 根据一级标签获取二级标签值
     *
     * @param tags
     * @return
     */
    public static String getTagValue(String parnet, String tags) {
        if (tags == null || parnet == null) {
            return null;
        }
        String[] tagsArray = tags.split(",");
        ArrayList<String> list = new ArrayList<String>();
        for (String tag : tagsArray) {
            String[] tagArray = tag.split(TagUtil.TAG_SPLIT);
            if (tagArray.length == 2 && parnet.equals(tagArray[0])) {
                list.add(tagArray[1]);
            }
        }
        return Joiner.on("、").join(list);
    }


    /**
     * 补全上层标签
     *
     * @param arg
     * @return
     */
    public static String fillTag(String arg) {
        if (arg == null || arg.length() == 0) {
            return "";
        }
        String[] tags = arg.split(",");
        String result = arg;
        for (int i = 0; i < tags.length; i++) {
            String[] t = tags[i].split(TAG_SPLIT);
            String tmp = "";
            for (int j = 0; j < t.length; j++) {
                if (j == 0) {
                    tmp = t[j];
                } else {
                    tmp = tmp + TAG_SPLIT + t[j];
                }
                if(!(","+result+",").contains(","+tmp+",")){
                    result = tmp + "," + result;
                }
            }
        }
        return result;
    }

    /**
     * 移出“全部”，如果全是全部，则标底二级 如果不全是全部，则移除全部
     *
     * @param arg
     * @return
     */
    public static String removeAllTag(String arg) {
        if (arg == null || arg.equals("") || arg.equals("全部") || arg.equals("全部" + TAG_SPLIT + "全部")
                || arg.equals("全部,") || arg.equals("全部" + TAG_SPLIT + "全部,")) {
            return "";
        }
        String[] tags = arg.split(",");
        int c = 0;
        for (String tag : tags) {
            if (tag.equals("全部") || tag.endsWith(TAG_SPLIT + "全部")) {
                c++;
            }
        }

        if (c == tags.length) {// 每个选中标签都是全部
            arg = (arg + ",").replace(TAG_SPLIT + "全部,", TAG_SPLIT + ",");
            if (arg.length() > 0 && !arg.equals(",")) {
                if (arg.startsWith(",")) {
                    arg = arg.substring(1);
                }
                return arg.substring(0, arg.length() - 1);
            } else {
                return "";
            }
        }

        String value = "";
        for (String tag : tags) {
            if (!tag.equals("全部") && !tag.endsWith(TAG_SPLIT + "全部")) {
                value += tag + ",";
            }
        }
        if (value.length() > 0 && !value.equals(",")) {
            if (value.startsWith(",")) {
                value = value.substring(1);
            }
            return value.substring(0, value.length() - 1);
        } else {
            return "";
        }
    }

    /**
     * 去掉重复的
     * @param color
     * @return
     */
    public static String removeRepeat(String color) {
        if (color == null || color.equals("") || !color.contains(",")) {
            return color;
        }
        String[] colors = color.split(",");
        List<String> list = new LinkedList<String>();
        for (int i = 0; i < colors.length; i++) {
            if (!list.contains(colors[i])) {
                list.add(colors[i]);
            }
        }

        String[] rowsTemp = list.toArray(new String[list.size()]);
        return StringUtils.join(rowsTemp, ",");
    }

    /**
     * 获取花型标题
     * @param tags
     * @return
     */
    public static String getFlowerTitle(String tags) {
        if (tags == null || tags.equals("")) {
            return "";
        }
        String[] tt = tags.split(",");
        String value = "";
        for (String t : tt) {
            String[] nn = t.split(TagUtil.TAG_SPLIT);
            if (nn.length == 2 && (nn[0].equals("主题") || nn[0].equals("内容"))) {
                if (value.equals(""))
                    value = nn[1];
                else
                    value = value + "、" + nn[1];
            }
        }
        return value;
    }


}
