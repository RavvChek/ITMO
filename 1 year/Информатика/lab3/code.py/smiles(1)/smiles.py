import re
x = input()
f = open(x + '.txt', 'r').read()
print(len(re.findall(r'[ ]+:-{P[ ]+', f)))
