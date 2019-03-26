cd out

for i in ./inputs/experiment1/* ./inputs/experiment2/* ./inputs/experiment3/*
do
	perf stat -e L1-dcache-loads,L1-dcache-load-misses,dTLB-loads,dTLB-load-misses,cache-misses java -Xmx4g -cp . s2imf35.Main -game=$i -linear -strategy=2 -timeout=30 > /dev/null

	perf stat -e L1-dcache-loads,L1-dcache-load-misses,dTLB-loads,dTLB-load-misses,cache-misses java -Xmx4g -cp . s2imf35.Main -game=$i -linear -strategy=3 -timeout=30 > /dev/null

	perf stat -e L1-dcache-loads,L1-dcache-load-misses,dTLB-loads,dTLB-load-misses,cache-misses java -Xmx4g -cp . s2imf35.Main -game=$i -linear -strategy=4 -timeout=30 > /dev/null

	perf stat -e L1-dcache-loads,L1-dcache-load-misses,dTLB-loads,dTLB-load-misses,cache-misses java -Xmx4g -cp . s2imf35.Main -game=$i -linear -strategy=6 -timeout=30 > /dev/null
done
