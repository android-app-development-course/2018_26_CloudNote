package com.example.note.mynote;

import java.util.HashMap;
import java.util.Map;

//地区选择数据源
public class GetCity {
    public Map<String,String[]> map = new HashMap<>();
    String []value1 = new String[]{"石家庄","保定市","秦皇岛","唐山市","邯郸市","邢台市","沧州市","承德市","廊坊市","衡水市","张家口"};
    String []value2 = new String[]{"太原市","大同市","阳泉市","长治市","临汾市","晋中市","运城市","晋城市","忻州市","朔州市","吕梁市"};
    String []value3 = new String[]{"呼和浩特","呼伦贝尔","包头市","赤峰市","乌海市","通辽市","鄂尔多斯","乌兰察布","巴彦淖尔"};
    String []value4 = new String[]{"盘锦市","鞍山市","抚顺市","本溪市","铁岭市","锦州市","丹东市","辽阳市","葫芦岛","阜新市","朝阳市","营口市"};
    String []value5 = new String[]{"吉林市","通化市","白城市","四平市","辽源市","松原市","白山市"};
    String []value6 = new String[]{"伊春市","牡丹江","大庆市","鸡西市","鹤岗市","绥化市","双鸭山","七台河","佳木斯","黑河市","齐齐哈尔市"};
    String []value7 = new String[]{"无锡市","常州市","扬州市","徐州市","苏州市","连云港","盐城市","淮安市","宿迁市","镇江市","南通市","泰州市"};
    String []value8 = new String[]{"绍兴市","温州市","湖州市","嘉兴市","台州市","金华市","舟山市","衢州市","丽水市"};
    String []value9 = new String[]{"合肥市","羌湖市","毫州市","马鞍山","池州市","淮南市","淮北市","蚌埠市","巢湖市","安庆市","宿州市", "宣城市","滁州市",
            "黄山市","六安市","阜阳市","铜陵市"};
    String []value10 = new String[]{"福州市","泉州市","漳州市","南平市","三明市","龙岩市","莆田市","宁德市"};
    String []value11 = new String[]{"南昌市","赣州市","景德镇","九江市","萍乡市", "抚州市","宜春市","上饶市","鹰潭市","吉安市"};
    String []value12 = new String[]{"潍坊市","淄博市","威海市","枣庄市","泰安市","临沂市","东营市","济宁市","烟台市","菏泽市","日照市","德州市","聊城市",
            "滨州市","莱芜市"};
    String []value13 = new String[]{"郑州市","洛阳市","焦作市","商丘市","信阳市","新乡市","安阳市","开封市","漯河市","南阳市","鹤壁市","平顶市","濮阳市",
            "许昌市","周口市","三门峡","驻马市"};
    String []value14 = new String[]{"荆门市","咸宁市","襄樊市","荆州市","黄石市","宜昌市","随州市","鄂州市","孝感市","黄冈市","十堰市"};
    String []value15 = new String[]{"长沙市","郴州市","娄底市","衡阳市","株洲市","湘潭市","岳阳市","常德市","邵阳市","益阳市","永州市","张家界","怀化市"};
    String []value16 = new String[]{"江门市","佛山市","汕头市","湛江市","韶关市","中山市","珠海市","茂名市","肇庆市","阳江市","惠州市","潮州市","揭阳市",
            "清远市","河源市","东莞市","汕尾市","云浮市"};
    String []value17 = new String[]{"南宁市","贺州市","柳州市","桂林市","梧州市","北海市","玉林市","钦州市","百色市","防城港","贵港市","河池市","崇左市","来宾市"};
    String []value18 = new String[]{"海口市","三亚市"};
    String []value19 = new String[]{"乐山市","雅安市","广安市","南充市","自贡市","泸州市","内江市","宜宾市","广元市","达州市","资阳市","绵阳市","眉山市","巴中市",
            "攀枝花","遂宁市","德阳市"};
    String []value20 = new String[]{"贵阳市","安顺市","遵义市","六盘水"};
    String []value21 = new String[]{"昆明市","玉溪市","大理市","曲靖市","昭通市","保山市","丽江市","临沧市"};
    String []value22 = new String[]{"拉萨市","阿里"};
    String []value23 = new String[]{"咸阳市","榆林市","宝鸡市","铜川市","渭南市","汉中市","安康市","商洛市","延安市"};
    String []value24 = new String[]{"兰州市","白银市","武威市","金昌市","平凉市","张掖市","嘉峪关","酒泉市","庆阳市","定西市","陇南市","天水市"};
    String []value25 = new String[]{"西宁市"};
    String []value26 = new String[]{"银川市","固原市","青铜峡市","石嘴山市","中卫市"};
    String []value27 = new String[]{"乌鲁木齐","克拉玛依市"};
    String []value28 = new String[]{"北京"};
    String []value29 = new String[]{"上海"};
    String []value30 = new String[]{"天津"};
    String []value31 = new String[]{"重庆"};
    String []value32 = new String[]{"香港"};
    String []value33 = new String[]{"澳门"};
    String []value34 = new String[]{"台湾"};


    public GetCity(){
        initial();
    }

    public void initial(){
        map.put("河北省",value1);
        map.put("山西省",value2);
        map.put("内蒙古",value3);
        map.put("辽宁省",value4);
        map.put("吉林省",value5);
        map.put("黑龙江省",value6);
        map.put("江苏省",value7);
        map.put("浙江省",value8);
        map.put("安徽省",value9);
        map.put("福建省",value10);
        map.put("江西省",value11);
        map.put("山东省",value12);
        map.put("河南省",value13);
        map.put("湖北省",value14);
        map.put("湖南省",value15);
        map.put("广东省",value16);
        map.put("广西省",value17);
        map.put("海南省",value18);
        map.put("四川省",value19);
        map.put("贵州省",value20);
        map.put("云南省",value21);
        map.put("西藏",value22);
        map.put("陕西省",value23);
        map.put("甘肃省",value24);
        map.put("青海省",value25);
        map.put("宁夏",value26);
        map.put("新疆",value27);
        map.put("香港特别行政区",value32);
        map.put("台湾",value34);
        map.put("澳门特别行政区",value33);
        map.put("北京市",value28);
        map.put("上海市",value29);
        map.put("重庆市",value31);
        map.put("天津市",value30);
    }
}
