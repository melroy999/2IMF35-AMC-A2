cd out
mkdir logs
touch logs/log0.txt
touch logs/log1.txt
#touch logs/log3.txt
java -Xmx4g -Xms4g -cp . s2imf35.Main -all -linear -strategy=0 -timeout=30 -n=10 > logs/log0.txt
java -Xmx4g -Xms4g -cp . s2imf35.Main -all -linear -strategy=1 -timeout=30 -n=10 > logs/log1.txt
#java -Xmx4g -Xms4g -cp . s2imf35.Main -all -linear -strategy=3 -timeout=30 -n=10 > logs/log3.txt
