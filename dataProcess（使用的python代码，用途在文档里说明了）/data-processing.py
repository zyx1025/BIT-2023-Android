import pandas as pd
import re

# 读取原始的CSV文件
df = pd.read_csv('/kaggle/input/course/Courses.csv', encoding='utf-8')

# 创建一个字典，用于将星期的汉字转换为对应的数字
day_of_week_dict = {'一': 1, '二': 2, '三': 3, '四': 4, '五': 5, '六': 6, '日': 7}

# 遍历原始数据的每一行
data_dict = {}
last_classroom = None
for index, row in df.iterrows():
    # 对"已排时间地点"字段进行拆分
    for time_and_location in re.split(',|;', row['已排时间地点']):
        # 使用正则表达式解析时间和地点
        match = re.match(r'(\d+-\d+|\d+)(周\(单\)|周\(双\)|周) 星期([\u4e00-\u9fa5])\s?\[?(\d+)-(\d+)节\]?\s?文萃楼(.+)', time_and_location.strip())
        if match:
            weeks, week_type, day_of_week, start_time, end_time, classroom = match.groups()
            last_classroom = classroom.strip()  # Update the last classroom
            if '-' in weeks:
                start_week, end_week = map(int, weeks.split('-'))
                if week_type == '周(单)':
                    weeks = list(range(start_week, end_week+1, 2))
                elif week_type == '周(双)':
                    weeks = list(range(start_week, end_week+1, 2))
                    if start_week % 2 == 0:
                        weeks = [week+1 for week in weeks]
                else:
                    weeks = list(range(start_week, end_week+1))
            else:
                weeks = [int(weeks)]

            # 对于每一个周次，添加一条新的记录
            for week in weeks:
                data_dict[(week, day_of_week_dict[day_of_week], int(start_time), int(end_time), classroom)] = 0

    # 处理"调课情况"字段
    if not pd.isnull(row['调课结果']):
        for time_and_location in re.split(',|;', row['调课结果']):
            # Handle reschedule
            reschedule_parts = time_and_location.split(' 调至:')
            match = re.match(r'调课:(\d+|(\d+-\d+))周 星期(\d+) (\d+)-(\d+)节(.*?)/\d+ 文萃楼(.+)', reschedule_parts[0].strip())
            if match:
                week, _, day_of_week, start_time, end_time, _, classroom = match.groups()
                classroom = classroom.strip()
                if '-' in week:
                    start_week, end_week = map(int, week.split('-'))
                    weeks = list(range(start_week, end_week+1))
                else:
                    weeks = [int(week)]

                for week in weeks:
                    key = (week, int(day_of_week), int(start_time), int(end_time), classroom)
                    data_dict[key] = 1  # Mark the original course as rescheduled

                if len(reschedule_parts) > 1:
                    new_location = reschedule_parts[1]
                    match2 = re.match(r'(\d+|(\d+-\d+))周 星期(\d+) (\d+)-(\d+)节(.*?)/\d+ 文萃楼(.+)', new_location)
                    if match2:
                        new_week, _, new_day_of_week, new_start_time, new_end_time, _, new_classroom = match2.groups()
                        new_classroom = new_classroom.strip()
                        if '-' in new_week:
                            new_start_week, new_end_week = map(int, new_week.split('-'))
                            new_weeks = list(range(new_start_week, new_end_week+1))
                        else:
                            new_weeks = [int(new_week)]

                        for new_week in new_weeks:
                            new_key = (new_week, int(new_day_of_week), int(new_start_time), int(new_end_time), new_classroom)
                            data_dict[new_key] = 0  # Add new course

            # Handle make-up class
            match3 = re.match(r'补课:(\d+|(\d+-\d+))周 星期(\d+) (\d+)-(\d+)节', time_and_location.strip())
            if match3:
                week, _, day_of_week, start_time, end_time = match3.groups()
                if '-' in week:
                    start_week, end_week = map(int, week.split('-'))
                    weeks = list(range(start_week, end_week+1))
                else:
                    weeks = [int(week)]

                for week in weeks:
                    key = (week, int(day_of_week), int(start_time), int(end_time), last_classroom)
                    data_dict[key] = 0  # Add new course

            # Handle class cancellation
            match4 = re.match(r'停课:(\d+|(\d+-\d+))周 星期(\d+) (\d+)-(\d+)节(.*?)/\d+ 文萃楼(.+)', time_and_location.strip())
            if match4:
                week, _, day_of_week, start_time, end_time, _, classroom = match4.groups()
                classroom = classroom.strip()
                if '-' in week:
                    start_week, end_week = map(int, week.split('-'))
                    weeks = list(range(start_week, end_week+1))
                else:
                    weeks = [int(week)]

                for week in weeks:
                    key = (week, int(day_of_week), int(start_time), int(end_time), classroom)
                    data_dict[key] = 1  # Mark the original course as cancelled

# 从字典中生成新的DataFrame
new_df = pd.DataFrame([{'week': key[0],
                        'day_of_week': key[1],
                        'start_time': key[2],
                        'end_time': key[3],
                        'classroom': key[4],
                        'is_rescheduled': value}
                       for key, value in data_dict.items()])

# 将预处理后的数据保存为新的CSV文件
new_df.to_csv('PreprocessedCourses.csv', index=False, encoding='utf-8')