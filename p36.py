def binPal(n):
    if n == 1:
        return ["0","1"]
    ans = []
    temp = 0b0
    if n%2 == 0:
        for i in range(2**(n/2)):
            if len(str(bin(temp))[2:]) == n/2 and temp > 0:
                ans.append(str(bin(temp))[2:] + str(bin(temp))[2:][::-1])
            temp += 0b1
    else:
        for i in range(2**(n/2)):
            if len(str(bin(temp))[2:]) == n/2 and temp > 0:
                ans.append(str(bin(temp))[2:] + "0" + str(bin(temp))[2:][::-1])
                ans.append(str(bin(temp))[2:] + "1" + str(bin(temp))[2:][::-1])
            temp += 0b1
    return ans

def palUnder(n):
    length = len(str(bin(n))[2:])
    ans = 0
    
    for i in range(1,length+1):
        for j in binPal(i):
            if isPal(str(int(j,2))) and int(j,2) < n:
                ans += int(j,2)
    return ans

def isPal(n):
    return n == n[::-1]

def main():
    print palUnder(1000000)
