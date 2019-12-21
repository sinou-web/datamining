import matplotlib.pyplot as plt


def convertStringtoList(chainofcharacter):

    numbers = chainofcharacter.split(",")
    floatnumbers=[float(num.strip()) for num in numbers]
    return floatnumbers






k =[i for i in range(3,8)]


file = open("/home/masterubunto/M2_gitprojects/Data-Mining/knntest.txt/hassina.txt","r").readlines()
title=""
data="labor"
for line in file:
    if line.startswith("data"):
        data=line.strip()
    if line.startswith("Train"):
        title=line.strip()
        print(title)
    if line.startswith("time"):
        time=convertStringtoList(line.split("time:")[1])

        plt.bar(k, time, label="time", color=("#ffa224"))
        plt.legend()
        plt.xlabel('K variations')
        plt.ylabel('Time variations (mili secondes)')
     #   plt.style.use('dark_background')
        plt.title('Varations  du temps en fonction de k')
        plt.savefig(data+":"+title+'time.png', dpi=100)
        plt.close()
    if line.startswith("accuracy"):
        accuracy=convertStringtoList(line.split("accuracy:")[1])

        plt.bar(k, accuracy, label="accuracy", color=("r"))
        plt.legend()
        plt.xlabel('K variations')
        plt.ylabel('Accuracy variations')
      #  plt.style.use('dark_background')
        plt.title('Varations de la precision en fonction de k')
        plt.savefig(data+":"+title+":"+'accuracy.png', dpi=100)
        plt.close()

