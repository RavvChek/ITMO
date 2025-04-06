import re
x = input()
f = open(x + '.txt', 'r', encoding="utf-8").read()
match = re.split(r'[\s\W]+', f)
right_vowels = []
for i in range(len(match)):
    Ai = ''
    A = re.search(r'[аоуыэеёиюя]', match[i], flags=re.IGNORECASE)
    if A:
        Ai = A.group()
    vowels = re.findall(r'[аоуыэеёиюя]', match[i], flags=re.IGNORECASE)
    if len(vowels) == match[i].count(Ai) and match[i] != '' and len(vowels) != 0:
        right_vowels.append(match[i])
print(sorted(right_vowels, key=len))
