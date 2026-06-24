import urllib.request, json
try:
    print("Fetching weather HTML")
    w_html = urllib.request.urlopen('https://eth2.cnnd.vn/ajax/weatherinfo/20070076.htm').read().decode('utf-8')
    print('Weather snippet:', w_html[:1000])
except Exception as e:
    print(e)
