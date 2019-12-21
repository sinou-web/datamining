#!/usr/bin/env python3
# -*- coding: utf-8 -*-

import numpy as np
import matplotlib.pyplot as plt


def plot_line(x, y):
	plt.plot(np.unique(x), np.poly1d(np.polyfit(x, y, 2))(np.unique(x)))


""" DBSCAN """
#plt.title('Variation du temps d\'exécution selon taille du dataset')
#plt.axis([0, 4.5, 0, 800])
#
#plt.plot(["Labor(57/17)","IRIS(150/5)","Glass(214/10)","Vote(435/17)","Unbalanced(856/33)"], [3, 12, 24, 122, 779], 'go-', label='Temps', linewidth=3)
#
#fig_size = plt.rcParams["figure.figsize"]
#fig_size[0] = 7
#fig_size[1] = 3
#plt.rcParams["figure.figsize"] = fig_size
#
#plt.xlabel('DataSet (Instance/Attribut)')
#plt.ylabel('Temps d exécution (millisec)')
#plt.savefig('dbscanPlot.png')
#plt.clf()


""" apriori + fp-growth """

numbers = [3,4,5,6]

ap = [40.62 , 0.96 , 0.016 , 0]
fp = [5.01 , 0.60 , 0.006 , 0]

plt.title('Variation du temps d\'exécution selon supp_min')
plt.axis([3, 6, 0, 41])

line1, = plt.plot(numbers, ap, 'go-', label='Apriori', linewidth=3)
line2, = plt.plot(numbers, fp, 'ro-', label='fp-growth', linewidth=3)

fig_size = plt.rcParams["figure.figsize"]
fig_size[0] = 7
fig_size[1] = 4
plt.rcParams["figure.figsize"] = fig_size

plt.figlegend((line1, line2), ('Apriori', 'fp-growth'), 'upper right')

plt.xlabel('Supp_min')
plt.ylabel('Temps d exécution (millisec)')
plt.savefig('ApFpPlot.png')
plt.clf()
#plt.show()



