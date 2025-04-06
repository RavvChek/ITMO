def hamming_code(message):
    r1, r2, i1, r3, i2, i3, i4 = [int(x) for x in message]
    return int((str((r1+i1+i2+i4) % 2) + str((r2+i1+i3+i4) % 2) + str((r3+i2+i3+i4) % 2))[::-1], 2)

message = input()
if len(message) == 7 and (message.count('0') + message.count('1') == 7):
    wrong_m = hamming_code(message)
    if wrong_m == 0:
        print('В сообщении нет ошибок')
    else:
        m = ''
        for i in range(len(message)):
            m += str((int(message[i]) + (i == (wrong_m - 1)) * 1) % 2)
        print('В сообщении была ошибка в ' + str(wrong_m) + ' бите. Правильное сообщение:' + m)


else:
    print('Неправильный формат ввода')