from django.db import models

class LegalAidRequest(models.Model):
    CASE_TYPE_CHOICES = [
        ('CRIMINAL', 'Criminal Defense'),
        ('FAMILY', 'Family Law'),
        ('IMMIGRATION', 'Immigration'),
        ('HOUSING', 'Housing'),
        ('EMPLOYMENT', 'Employment'),
        ('CONSUMER', 'Consumer Rights'),
        ('CIVIL', 'Civil Rights'),
        ('OTHER', 'Other'),
    ]

    URGENCY_CHOICES = [
        ('HIGH', 'High - Immediate attention required'),
        ('MEDIUM', 'Medium - Within a week'),
        ('LOW', 'Low - General consultation'),
    ]

    case_type = models.CharField(max_length=20, choices=CASE_TYPE_CHOICES)
    urgency_level = models.CharField(max_length=10, choices=URGENCY_CHOICES)
    description = models.TextField()
    contact_info = models.TextField()
    created_at = models.DateTimeField(auto_now_add=True)
    status = models.CharField(max_length=20, default='PENDING')

    def __str__(self):
        return f"{self.case_type} - {self.created_at}" 