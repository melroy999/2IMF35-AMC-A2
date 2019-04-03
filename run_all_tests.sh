cd out
mkdir logs
touch logs/log5.txt
touch logs/log7.txt
#touch logs/log3.txt
java -Xmx4g -Xms4g -cp . s2imf35.Main -all -linear -strategy=5 -timeout=15 -n=10 > logs/log5.txt
java -Xmx4g -Xms4g -cp . s2imf35.Main -all -linear -strategy=7 -timeout=15 -n=10 > logs/log7.txt
#java -Xmx4g -Xms4g -cp . s2imf35.Main -all -linear -strategy=3 -timeout=30 -n=10 > logs/log3.txt
