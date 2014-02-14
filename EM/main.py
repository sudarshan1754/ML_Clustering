
import math, random
import numpy as np 

filepath = '/home/sid/Desktop/5ML/em_data.txt'

def read_integers(filename):
	return map(float, open(filename).read().split())

def zerolistmaker(n):
    listofzeros = [0] * n
    return listofzeros


#Get all datapoints and store it in list
data_points = read_integers(filepath)

"""
print len(m) = 6000
print min(m) = 2.111666875
print max(m) = 28.94874237
"""

# Big_Theta = [a1, a2, (m1, m2, v1, v2) = Theta_k]

k = 3

alpha_list = []


alpha1 = random.random() / 2
alpha_list.append(alpha1)
alpha_list.append(alpha1)
alpha_list.append(1 - (alpha1 + alpha1))

#print alpha_list

mean_list = []
for each_mean in range(0, k):
	val_ind = int(random.randrange(0, len(data_points)))
	mean_list.append(data_points[int(val_ind)])


#To make one of the Z values 1
index_for_z = random.randrange(0,k)
Z_list = zerolistmaker(k)
Z_list[index_for_z] = 1


covariance_list = []
for mean in mean_list:
	su = 0.0
	for xi in data_points:
		var_xi = (xi - mean) ** 2
		su = su + var_xi

	covar = su / len(data_points)
	covariance_list.append(covar)

#print alpha_list, mean_list, Z_list, covariance_list




def gauss(var, mean, dp):
	Nr = math.exp(-0.5) * var * (dp - mean)
	Dr = ((2 * math.pi) ** (0.5)) * (var ** 0.5) 
	pk = Nr / Dr
	return pk

#E-Step
stopping = []
for iteration in range(500):
	pik_list = []
	for a, entry in enumerate(alpha_list):
		pik = []
		for xi  in data_points:
			pk = alpha_list[a] * (gauss(covariance_list[a], mean_list[a], xi))
			pik.append(pk)
		pik_list.append(pik)

	wik_list = []
	w_add = []
	for i, ent in enumerate(pik_list[0]):
			den = ent + pik_list[1][i]
			w_add.append(den)


	for k in pik_list:
		wik_k = []
		for i, pk in enumerate(k):

			wik = pk / w_add[i]
			wik_k.append(wik)
		wik_list.append(wik_k)



	#M-Step
	
	for i, wik in enumerate(wik_list):
		su = 0.0
		for wgts in wik:
			su += wgts
		d = su/len(data_points)
		alpha_list[i] = d
		

	#print alpha_list

	for i, apl in enumerate(alpha_list):
		su = 0.0
		Nk = 1.0
		for j, wik in enumerate(wik_list[i]):
			su = su + (wik * data_points[j])
			Nk = Nk + wik
		mean_list[i] = su / Nk

	#print mean_list
	
	for m, cov in enumerate(covariance_list):
		su = 0.0
		Nk = 1.0
		for j, wik in enumerate(wik_list[i]):
			su = su + (wik * (data_points[j] - mean_list[m]))
			Nk = Nk + wik
		covariance_list[m] = abs(su / Nk)

	#print covariance_list
	#Log Likelihood
	for xi in data_points:
		loga = []
		sum_k = 0.0
		for i, ak in enumerate(alpha_list):
			su = 0.0 
			for pk in pik_list[i]:
				su += ak * pk
			sum_k += su
		loga.append(math.log(abs(sum_k),2))


	
	stopping.append(sum(loga))
	#print "Log Likelihood: ",
	#print sum(loga)
	print "Iteration No: " + str(iteration + 1) + " Done"
	if iteration >2:
		last = stopping[-1]
		prev = stopping[-2]
		diff = abs(last - prev)
		if diff < 0.50:
			print "******************************************************"
			print "Parameters Obtained "
			print "Alphas: ",
			print alpha_list
			print "Means: ",
			print mean_list
			print "Variance: ",
			print covariance_list
			break
			#break

	
		#break