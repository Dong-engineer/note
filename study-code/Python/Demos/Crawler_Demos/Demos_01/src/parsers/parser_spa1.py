# 解析 spa1 的 json 数据

def parser_spa1_json(json_data):
    # 获取结果键值对，其中包含所有电影信息
    all_data = json_data["results"]

    infos = []
    for data in all_data:
        # 单个电影对象信息
        info = {}
        info.setdefault("电影名称", data["name"])
        info.setdefault("网页地址", data["cover"])
        info.setdefault("地区", data["regions"])
        info.setdefault("时长", data["minute"])
        info.setdefault("上映时间", data["published_at"])
        info.setdefault("评分", data["score"])

        infos.append(info)

    return infos
