import re
x = input()
f = open(x + '.txt', 'r', encoding="utf-8").read()
match = re.findall(r'[аоуыэеёиюя]', f, flags=re.IGNORECASE)
if len(match) == 17:
    match = re.findall(r'/', f)
    if len(match) == 2:
        match = re.split(r'/', f)
        flag = 0
        for i in range(len(match)):
            if (len(re.findall(r'[аоуыэеёиюя]', match[i], flags=re.IGNORECASE)) == 5 and i % 2 == 0) or (len(re.findall(r'[аоуыэеёиюя]', match[i], flags=re.IGNORECASE)) == 7 and i % 2 == 1):
                flag = 1
            else:
                print('Не хайку.')
                break
        if flag == 1:
            print("Хайку!")

    else:
        print('Не хайку. Должно быть 3 строки.')
else:
    print('Не хайку.')
