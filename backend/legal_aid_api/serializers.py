from rest_framework import serializers
from .models import LegalAidRequest

class LegalAidRequestSerializer(serializers.ModelSerializer):
    class Meta:
        model = LegalAidRequest
        fields = ['id', 'case_type', 'urgency_level', 'description', 'contact_info', 'created_at', 'status']
        read_only_fields = ['id', 'created_at', 'status'] 