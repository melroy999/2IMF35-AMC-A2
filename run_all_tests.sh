cd out
mkdir logs
touch logs/log2.txt
touch logs/log3.txt
touch logs/log4.txt
touch logs/log5.txt
touch logs/log6.txt
touch logs/log7.txt
java -Xmx4g -Xms4g -cp . s2imf35.Main -all -linear -strategy=2 -timeout=30 -n=10 > logs/log2.txt
java -Xmx4g -Xms4g -cp . s2imf35.Main -all -linear -strategy=3 -timeout=30 -n=10 > logs/log3.txt
java -Xmx4g -Xms4g -cp . s2imf35.Main -all -linear -strategy=4 -timeout=30 -n=10 > logs/log4.txt
java -Xmx4g -Xms4g -cp . s2imf35.Main -all -linear -strategy=5 -timeout=30 -n=10 > logs/log5.txt
java -Xmx4g -Xms4g -cp . s2imf35.Main -all -linear -strategy=6 -timeout=30 -n=10 > logs/log6.txt
java -Xmx4g -Xms4g -cp . s2imf35.Main -all -linear -strategy=7 -timeout=30 -n=10 > logs/log7.txt
