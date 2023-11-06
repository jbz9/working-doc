package com.jiang.learn.issue;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FastJson {

    public static void main(String[] args) {
        Map<String, Object> cbtMap = new HashMap<>();
        cbtMap.put("key", "image");
        cbtMap.put("name", "回复图片");
        cbtMap.put("type", "click");
        JSONObject cbtJson = new JSONObject(cbtMap);


        Map<String, Object> vbtMap = new HashMap<>();
        vbtMap.put("url", "http://www.cuiyongzhi.com");
        vbtMap.put("name", "博客");
        vbtMap.put("type", "view");
        JSONObject vbtJson = new JSONObject(vbtMap);

        JSONArray subButton = new JSONArray();
        subButton.add(cbtJson);
        subButton.add(vbtJson);

        JSONObject buttonOne = new JSONObject();
        buttonOne.put("name", "菜单");
        buttonOne.put("sub_button", subButton);

        JSONArray button = new JSONArray();
        button.add(vbtJson);
        button.add(buttonOne);
        button.add(cbtJson);

        JSONObject menujson = new JSONObject();
        menujson.put("button", button);


        System.out.println(menujson.toJSONString());
    }


}
