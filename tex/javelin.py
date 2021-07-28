from math import sin, cos

theta = 0.1    # our initial guess
gamma = 0.001  # learning rate

distance = 80 * sin(2 * theta)
print(theta, distance)

for i in range(100):
	dtheta = 160 * cos(2 * theta)
	theta = theta + gamma * dtheta

	distance = 80 * sin(2 * theta)
	print(theta, distance)